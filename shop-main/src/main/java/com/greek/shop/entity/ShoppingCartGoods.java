package com.greek.shop.entity;

import com.greek.shop.generate.Goods;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/4 16:47
 */
public class ShoppingCartGoods extends Goods {
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
