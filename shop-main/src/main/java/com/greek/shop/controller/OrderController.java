package com.greek.shop.controller;

import com.greek.shop.api.data.OrderInfo;
import com.greek.shop.entity.OrderResponse;
import com.greek.shop.service.OrderService;
import com.greek.shop.service.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/18 9:29
 */
@RestController
@RequestMapping("/api/v1")
public class OrderController {
    @Autowired
    private OrderService orderService;


    @PostMapping("/order")
    public OrderResponse createOrder(@RequestBody OrderInfo orderInfo) {
        orderService.deductStock(orderInfo);
        return orderService.createOrder(orderInfo, UserContext.getCurrentUser().getId());
    }

}
