package com.greek.shop.api.rpc;

import com.greek.shop.api.data.OrderInfo;
import com.greek.shop.api.data.Page;
import com.greek.shop.api.data.RpcOrderGoods;
import com.greek.shop.api.enums.StatusEnum;
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
     * @param order     需要创建的订单
     * @return 创建好的订单信息
     */
    Order createOrder(OrderInfo orderInfo, Order order);

    /**
     * 删除订单接口
     *
     * @param orderId 订单id
     * @param userId  用户id
     * @return 删除的订单信息
     */
    RpcOrderGoods deleteOrder(long orderId, long userId);

    /**
     * 分页获取订单商品信息
     *
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @param status   数据状态
     * @return 订单商品信息
     */
    Page<RpcOrderGoods> getOrder(long userId, Integer pageNum, Integer pageSize, StatusEnum status);


    /**
     * 根据订单id获取订单信息
     *
     * @param orderId 订单id
     * @return 订单信息
     */
    Order getOrderById(long orderId);

    /**
     * 修改订单信息
     *
     * @param order 需要修改的订单信息
     * @return 修改好的订单信息
     */
    RpcOrderGoods updateOrder(Order order);
}
