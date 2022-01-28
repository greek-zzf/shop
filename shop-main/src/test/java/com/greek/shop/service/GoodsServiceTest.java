package com.greek.shop.service;

import com.greek.shop.api.enums.StatusEnum;
import com.greek.shop.api.data.Page;
import com.greek.shop.api.excepitons.HttpException;
import com.greek.shop.generate.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    @Mock
    Shop shop;
    @Mock
    Goods goods;
    @InjectMocks
    GoodsService goodsService;

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
    }

    @Test
    void createGoodsFailedIfUserIsNotOwner() {
        when(shopMapper.selectByPrimaryKey(anyLong())).thenReturn(shop);
        when(shop.getOwnerUserId()).thenReturn(2L);

        HttpException forbiddenException = assertThrows(HttpException.class, () -> goodsService.createGoods(goods));
        assertEquals(forbiddenException.getMessage(), "无权访问！");
    }


    @Test
    void deleteGoodsThrowsNotFoundExceptionIfNotOwner() {
        when(goodsMapper.selectByPrimaryKey(anyLong())).thenReturn(null);
        HttpException notFoundException = assertThrows(HttpException.class, () -> goodsService.deleteGoodsById(anyLong()));
        assertEquals(notFoundException.getMessage(), "商品未找到！");
    }

    @Test
    void deleteGoodsSucceed() {
        long goodsToBeDeleted = 123;
        when(goodsMapper.selectByPrimaryKey(anyLong())).thenReturn(goods);
        when(goods.getShopId()).thenReturn(goodsToBeDeleted);
        when(shopMapper.selectByPrimaryKey(goodsToBeDeleted)).thenReturn(shop);
        when(shop.getOwnerUserId()).thenReturn(1L);

        goodsService.deleteGoodsById(goodsToBeDeleted);
        verify(goods).setStatus(StatusEnum.DELETE.toString());
        verify(goodsMapper).updateByPrimaryKey(goods);
    }

    @Test
    void updateGoodsSucceed() {
        when(shopMapper.selectByPrimaryKey(anyLong())).thenReturn(shop);
        when(shop.getOwnerUserId()).thenReturn(1L);
        when(goodsMapper.updateByPrimaryKey(goods)).thenReturn(1);

        assertEquals(goods, goodsService.updateGoods(goods));
    }


    @Test
    void updateGoodsFailed() {
        HttpException notFoundException = assertThrows(HttpException.class, () -> goodsService.deleteGoodsById(anyLong()));
        assertEquals(notFoundException.getMessage(), "商品未找到！");
    }

    @Test
    void getGoodsPage() {
        int pageNumber = 5;
        int pageSize = 10;
        List<Goods> mockGoodsList = Mockito.mock(List.class);

        when(goodsMapper.countByExample(any())).thenReturn(55L);
        when(goodsMapper.selectByExample(any())).thenReturn(mockGoodsList);

        Page<Goods> goodsPage = goodsService.getGoodsPage(pageNumber, pageSize, 456L);
        assertEquals(goodsPage.getPageNum(), 5);
        assertEquals(goodsPage.getPageSize(), 10);
        assertEquals(goodsPage.getTotalPage(), 6);
        assertEquals(goodsPage.getData(), mockGoodsList);
    }

}