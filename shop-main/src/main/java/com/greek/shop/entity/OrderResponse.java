package com.greek.shop.entity;

import com.greek.shop.api.generate.Order;
import com.greek.shop.generate.Shop;

import java.util.List;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/20 16:30
 */
public class OrderResponse extends Order {

    private Shop shop;
    private List<GoodsWithNumber> goods;

    public OrderResponse() {
    }

    public OrderResponse(Order order) {
        this.setId(order.getId());
        this.setUserId(order.getUserId());
        this.setTotalPrice(order.getTotalPrice());
        this.setAddress(order.getAddress());
        this.setExpressCompany(order.getExpressCompany());
        this.setExpressId(order.getExpressId());
        this.setStatus(order.getStatus());
        this.setCreatedAt(order.getCreatedAt());
        this.setUpdatedAt(order.getUpdatedAt());
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<GoodsWithNumber> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsWithNumber> goods) {
        this.goods = goods;
    }
}
