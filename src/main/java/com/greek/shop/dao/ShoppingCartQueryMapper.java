package com.greek.shop.dao;

import com.greek.shop.entity.ShoppingCartData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Zhaofeng Zhou
 * @date 2021/12/31 18:04
 */
@Mapper
public interface ShoppingCartQueryMapper {
    int countShopInUserShoppingCart(@Param("userId") long userId);

    List<ShoppingCartData> selectShoppingCartDataByUserId(@Param("userId") long userId,
                                                          @Param("offset") int offset,
                                                          @Param("limit") int limit);

    List<ShoppingCartData> selectShoppingCartDataByUserIdShopId(@Param("userId") long userId,
                                                                @Param("shopId") long shopId);
}
