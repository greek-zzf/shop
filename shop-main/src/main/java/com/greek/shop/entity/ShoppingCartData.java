package com.greek.shop.entity;

import com.greek.shop.generate.Shop;

import java.util.List;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/4 16:47
 */
public class ShoppingCartData {
    Shop shop;
    List<GoodsWithNumber> goods;

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
