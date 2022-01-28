package com.greek.shop.api.data;

import com.greek.shop.api.generate.Order;

import java.io.Serializable;
import java.util.List;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/28 14:41
 */
public class RpcOrderGoods implements Serializable {

    private Order order;
    private List<GoodsInfo> goods;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<GoodsInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsInfo> goods) {
        this.goods = goods;
    }
}
