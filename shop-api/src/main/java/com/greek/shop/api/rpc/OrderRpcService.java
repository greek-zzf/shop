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

    /**
     * 创建订单接口
     *
     * @param orderInfo 订单关联的商品以及对应数量信息
     * @param order 需要创建的订单
     * @return 创建好的订单信息
     */
    Order createOrder(OrderInfo orderInfo, Order order);
}
