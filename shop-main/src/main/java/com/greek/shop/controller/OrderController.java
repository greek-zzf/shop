package com.greek.shop.controller;

import com.greek.shop.api.data.OrderInfo;
import com.greek.shop.api.data.Page;
import com.greek.shop.api.enums.StatusEnum;
import com.greek.shop.api.excepitons.HttpException;
import com.greek.shop.api.generate.Order;
import com.greek.shop.entity.OrderResponse;
import com.greek.shop.service.OrderService;
import com.greek.shop.service.UserContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/order/{id}")
    public OrderResponse deleteOrder(@PathVariable("id") long orderId) {
        return orderService.deleteOrder(orderId, UserContext.getCurrentUser().getId());
    }

    @GetMapping("/order")
    public Page<OrderResponse> getOrder(@RequestParam("pageNum") Integer pageNum,
                                        @RequestParam("pageSize") Integer pageSize,
                                        @RequestParam(value = "status", required = false) String status) {

        if (StringUtils.isNotEmpty(status) && StatusEnum.fromStringValue(status) == null) {
            throw HttpException.badRequest("非法status: " + status);
        }

        return orderService.getOrder(pageNum, pageSize, StatusEnum.fromStringValue(status));
    }

    @PatchMapping("/order")
    public OrderResponse updateOrder(@RequestBody Order order) {
        if (order.getExpressCompany() != null) {
            return orderService.updateExpressInfomation(order, UserContext.getCurrentUser().getId());
        } else {
            return orderService.updateOrderStatus(order, UserContext.getCurrentUser().getId());
        }
    }

}
