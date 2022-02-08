package com.greek.order.service;

import com.greek.order.mapper.OrderBatchMapper;
import com.greek.shop.api.data.GoodsInfo;
import com.greek.shop.api.data.OrderInfo;
import com.greek.shop.api.data.Page;
import com.greek.shop.api.data.RpcOrderGoods;
import com.greek.shop.api.enums.StatusEnum;
import com.greek.shop.api.excepitons.HttpException;
import com.greek.shop.api.generate.*;
import com.greek.shop.api.rpc.OrderRpcService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

import static com.greek.shop.api.enums.StatusEnum.DELETE;
import static com.greek.shop.api.enums.StatusEnum.PENDING;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/18 14:21
 */
@DubboService(version = "${shop.orderservice.version}")
public class RpcOrderServiceImpl implements OrderRpcService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderBatchMapper orderBatchMapper;
    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    @Override
    public Order createOrder(OrderInfo orderInfo, Order order) {
        insertOrder(order);
        orderInfo.setOrderId(order.getId());
        orderBatchMapper.insertOrders(orderInfo.getGoods());
        return order;
    }

    @Override
    public RpcOrderGoods deleteOrder(long orderId, long userId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (null == order) {
            throw HttpException.forbidden("订单未找到: " + orderId);
        }

        if (order.getUserId() != userId) {
            throw HttpException.forbidden("无权访问! ");
        }

        List<GoodsInfo> goodsInfo = orderBatchMapper.getGoodsInfoOfOrder(orderId);

        order.setStatus(DELETE.getName());
        order.setUpdatedAt(new Date());
        orderMapper.updateByPrimaryKey(order);

        RpcOrderGoods result = new RpcOrderGoods();
        result.setGoods(goodsInfo);
        result.setOrder(order);
        return result;
    }

    @Override
    public Page<RpcOrderGoods> getOrder(long userId, Integer pageNum, Integer pageSize, StatusEnum status) {
        OrderExample countByStatus = new OrderExample();
        setStatus(countByStatus, status);
        int count = (int) orderMapper.countByExample(countByStatus);

        OrderExample pageOrder = new OrderExample();
        pageOrder.setOffset((pageNum - 1) * pageSize);
        pageOrder.setLimit(pageSize);
        setStatus(pageOrder, status);

        List<Order> orders = orderMapper.selectByExample(pageOrder);

        List<Long> orderIds = orders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        OrderGoodsExample selectByOrderIds = new OrderGoodsExample();
        selectByOrderIds.createCriteria().andIdIn(orderIds);
        List<OrderGoods> orderGoods = orderGoodsMapper.selectByExample(selectByOrderIds);

        int totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;

        Map<Long, List<OrderGoods>> orderIdToGoodsMap = orderGoods
                .stream()
                .collect(groupingBy(OrderGoods::getOrderId, toList()));

        List<RpcOrderGoods> rpcOrderGoods = orders.stream()
                .map(order -> toRpcOrderGoods(order, orderIdToGoodsMap))
                .collect(toList());

        return Page.of(pageNum, pageSize, totalPage, rpcOrderGoods);
    }

    @Override
    public Order getOrderById(long orderId) {
        return orderMapper.selectByPrimaryKey(orderId);
    }

    @Override
    public RpcOrderGoods updateOrder(Order order) {
        order.setUpdatedAt(new Date());
        orderMapper.updateByPrimaryKeySelective(order);

        List<GoodsInfo> goodsInfo = orderBatchMapper.getGoodsInfoOfOrder(order.getId());

        RpcOrderGoods result = new RpcOrderGoods();
        result.setGoods(goodsInfo);
        result.setOrder(orderMapper.selectByPrimaryKey(order.getId()));
        return result;
    }

    private RpcOrderGoods toRpcOrderGoods(Order order, Map<Long, List<OrderGoods>> orderIdToGoodsMap) {
        RpcOrderGoods result = new RpcOrderGoods();
        result.setOrder(order);
        List<GoodsInfo> goodsInfos = orderIdToGoodsMap.getOrDefault(order.getId(), Collections.emptyList())
                .stream()
                .map(this::toGoodsInfo)
                .collect(toList());
        result.setGoods(goodsInfos);
        return result;
    }

    private GoodsInfo toGoodsInfo(OrderGoods orderGoods) {
        GoodsInfo result = new GoodsInfo();
        result.setId(orderGoods.getGoodsId());
        result.setNumber(orderGoods.getNumber().intValue());
        return result;
    }

    private OrderExample.Criteria setStatus(OrderExample orderExample, StatusEnum statusEnum) {
        if (null == statusEnum) {
            return orderExample.createCriteria().andStatusNotEqualTo(DELETE.getName());
        } else {
            return orderExample.createCriteria().andStatusEqualTo(statusEnum.getName());
        }
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

        long id = orderMapper.insert(order);
        order.setId(id);
    }

    private void verify(BooleanSupplier supplier, String message) {
        if (supplier.getAsBoolean()) {
            throw new IllegalArgumentException(message);
        }
    }
}