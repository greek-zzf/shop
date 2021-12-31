package com.greek.shop.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Zhaofeng Zhou
 * @date 2021/12/31 18:04
 */
@Mapper
public interface ShoppingCartQueryMapper {
    int countShopInUserShoppingCart(@Param("userId") long userId);
}
