package com.greek.shop.api.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/20 9:34
 */
public class OrderInfo implements Serializable {

    private List<GoodsInfo> goods;

    public List<GoodsInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsInfo> goods) {
        this.goods = goods;
    }
}
