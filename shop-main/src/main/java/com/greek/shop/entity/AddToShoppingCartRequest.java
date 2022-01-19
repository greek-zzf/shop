package com.greek.shop.entity;

import java.util.List;

public class AddToShoppingCartRequest {
    List<ShoppingCartGoods> goods;

    public List<ShoppingCartGoods> getGoods() {
        return goods;
    }

    public void setGoods(List<ShoppingCartGoods> goods) {
        this.goods = goods;
    }
}
