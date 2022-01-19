package com.greek.shop.entity;

import com.greek.shop.generate.Shop;

import java.util.List;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/4 16:47
 */
public class ShoppingCartData {
    Shop shop;
    List<ShoppingCartGoods> goods;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<ShoppingCartGoods> getGoods() {
        return goods;
    }

    public void setGoods(List<ShoppingCartGoods> goods) {
        this.goods = goods;
    }
}
