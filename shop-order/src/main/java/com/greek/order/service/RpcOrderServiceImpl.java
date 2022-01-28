package com.greek.order.service;

import com.greek.order.mapper.OrderBatchMapper;
import com.greek.shop.api.data.GoodsInfo;
import com.greek.shop.api.data.OrderInfo;
import com.greek.shop.api.data.RpcOrderGoods;
import com.greek.shop.api.excepitons.HttpException;
import com.greek.shop.api.generate.Order;
import com.greek.shop.api.generate.OrderMapper;
import com.greek.shop.api.rpc.OrderRpcService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.function.BooleanSupplier;

import static com.greek.shop.api.enums.StatusEnum.DELETE;
import static com.greek.shop.api.enums.StatusEnum.PENDING;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/18 14:21
 */
@DubboService(version = "${shop.orderservice.version}")
public class RpcOrderServiceImpl implements OrderRpcService {

    @Autowired
    private OrderMapper orderMapperr;
    @Autowired
    private OrderBatchMapper orderBatchMapper;

    @Override
    public Order createOrder(OrderInfo orderInfo, Order order) {
        insertOrder(order);
        orderInfo.setOrderId(order.getId());
        orderBatchMapper.insertOrders(orderInfo.getGoods());
        return order;
    }

    @Override
    public RpcOrderGoods deleteOrder(long orderId, long userId) {
        Order order = orderMapperr.selectByPrimaryKey(orderId);
        if (null == order) {
            throw HttpException.forbidden("订单未找到: " + orderId);
        }

        if (order.getUserId() != userId) {
            throw HttpException.forbidden("无权访问! ");
        }

        List<GoodsInfo> goodsInfo = orderBatchMapper.getGoodsInfoOfOrder(orderId);

        order.setStatus(DELETE.getName());
        order.setUpdatedAt(new Date());
        orderMapperr.updateByPrimaryKey(order);

        RpcOrderGoods result = new RpcOrderGoods();
        result.setGoods(goodsInfo);
        result.setOrder(order);
        return result;
    }

    private void insertOrder(Order order) {
        order.setStatus(PENDING.getName());

        verify(() -> order.getUserId() == null, "userId不能为空！！");
        verify(() -> order.getTotalPrice() == null && order.getTotalPrice().doubleValue() < 0, "totalPrice 非法");
        verify(() -> order.getAddress() == null, "address不能为空");

        order.setExpressCompany(null);
        order.setExpressId(null);
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());

        long id = orderMapperr.insert(order);
        order.setId(id);
    }

    private void verify(BooleanSupplier supplier, String message) {
        if (supplier.getAsBoolean()) {
            throw new IllegalArgumentException(message);
        }
    }
}