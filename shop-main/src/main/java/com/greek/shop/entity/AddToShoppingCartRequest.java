package com.greek.shop.entity;

import java.util.List;

public class AddToShoppingCartRequest {
    List<GoodsWithNumber> goods;

    public List<GoodsWithNumber> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsWithNumber> goods) {
        this.goods = goods;
    }
}
