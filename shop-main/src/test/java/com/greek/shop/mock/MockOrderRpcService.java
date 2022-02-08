package com.greek.shop.mock;

import com.greek.shop.api.data.OrderInfo;
import com.greek.shop.api.data.Page;
import com.greek.shop.api.data.RpcOrderGoods;
import com.greek.shop.api.enums.StatusEnum;
import com.greek.shop.api.generate.Order;
import com.greek.shop.api.rpc.OrderRpcService;
import org.apache.dubbo.config.annotation.DubboService;
import org.mockito.Mock;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/26 16:02
 */
@DubboService(version = "${shop.orderservice.version}")
public class MockOrderRpcService implements OrderRpcService {

    @Mock
    public OrderRpcService orderRpcService;


    @Override
    public Order createOrder(OrderInfo orderInfo, Order order) {
        return orderRpcService.createOrder(orderInfo, order);
    }

    @Override
    public RpcOrderGoods deleteOrder(long orderId, long userId) {
        return orderRpcService.deleteOrder(orderId, userId);
    }

    @Override
    public Page<RpcOrderGoods> getOrder(Integer pageNum, Integer pageSize, StatusEnum status) {
        return orderRpcService.getOrder(pageNum, pageSize, status);
    }
}
