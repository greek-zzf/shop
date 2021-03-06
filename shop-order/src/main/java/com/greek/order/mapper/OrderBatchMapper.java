package com.greek.order.mapper;

import com.greek.shop.api.data.GoodsInfo;
import com.greek.shop.api.data.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/20 16:03
 */
@Mapper
public interface OrderBatchMapper {

    void insertOrders(OrderInfo orderInfo);

    List<GoodsInfo> getGoodsInfoOfOrder(long orderId);

}
