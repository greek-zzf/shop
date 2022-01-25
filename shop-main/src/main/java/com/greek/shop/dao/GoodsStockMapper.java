package com.greek.shop.dao;

import com.greek.shop.api.data.GoodsInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/25 10:19
 */
@Mapper
public interface GoodsStockMapper {

    int deductStock(GoodsInfo goodsInfo);
}
