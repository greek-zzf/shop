package com.greek.shop.api.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/20 9:34
 */
public class OrderInfo implements Serializable {
    private long orderId;
    private List<GoodsInfo> goods;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public List<GoodsInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsInfo> goods) {
        this.goods = goods;
    }
}
