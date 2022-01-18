package com.greek.order.service;

import com.greek.shop.api.rpc.OrderService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/18 14:21
 */
@DubboService(version = "${shop.orderservice.version}")
public class RpcOrderServiceImpl implements OrderService {

    @Override
    public String sayHello(String name) {
        return "我是" + name;
    }
}