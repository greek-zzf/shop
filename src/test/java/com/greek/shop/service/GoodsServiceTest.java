package com.greek.shop.service;

import com.greek.shop.dao.GoodsMapper;
import com.greek.shop.dao.ShopMapper;
import com.greek.shop.entity.Goods;
import com.greek.shop.entity.Shop;
import com.greek.shop.entity.User;
import com.greek.shop.exception.HttpException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Zhaofeng Zhou
 * @date 19/11/2021 下午9:41
 */
@ExtendWith({MockitoExtension.class})
class GoodsServiceTest {

    @Mock
    GoodsMapper goodsMapper;
    @Mock
    ShopMapper shopMapper;
    @InjectMocks
    GoodsService goodsService;
    @Mock
    Shop shop;
    @Mock
    Goods goods;

    @BeforeEach
    void initUserContext() {
        User currentUser = new User();
        currentUser.setId(1L);
        UserContext.setCurrentUser(currentUser);
    }

    @AfterEach
    void clearUserContext() {
        UserContext.setCurrentUser(null);
    }


    @Test
    void createGoodsSucceedIfUserIsOwner() {
        when(shopMapper.selectByPrimaryKey(anyLong())).thenReturn(shop);
        when(shop.getOwnerUserId()).thenReturn(1L);
        when(goodsMapper.insert(goods)).thenReturn(123);

        assertEquals(goods, goodsService.createGoods(goods));
        verify(goods).setId(123L);

    }

    @Test
    void createGoodsFailedIfUserIsNotOwner() {
        when(shopMapper.selectByPrimaryKey(anyLong())).thenReturn(shop);
        when(shop.getOwnerUserId()).thenReturn(2L);

        assertThrows(HttpException.class, () -> goodsService.createGoods(goods));
    }


    @Test
    void deleteGoodsById() {
    }

    @Test
    void updateGoods() {
    }

    @Test
    void getGoodsPage() {
    }
}