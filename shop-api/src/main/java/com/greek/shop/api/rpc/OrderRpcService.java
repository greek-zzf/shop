package com.greek.shop.api.rpc;

import com.greek.shop.api.data.OrderInfo;
import com.greek.shop.api.generate.Order;

/**
 * rpc 下单模块
 *
 * @author Zhaofeng Zhou
 * @date 2022/1/18 9:22
 */
public interface OrderRpcService {

    Order createOrder(OrderInfo orderInfo, Order order);
}
