package com.greek.shop.service;

import com.greek.shop.dao.GoodsMapper;
import com.greek.shop.dao.ShopMapper;
import com.greek.shop.entity.Goods;
import com.greek.shop.entity.Shop;
import com.greek.shop.enums.StatusEnum;
import com.greek.shop.exception.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/15/015 14:51
 */
@Service
public class GoodsService {

    private final GoodsMapper goodsMapper;
    private final ShopMapper shopMapper;

    @Autowired
    public GoodsService(GoodsMapper goodsMapper, ShopMapper shopMapper) {
        this.goodsMapper = goodsMapper;
        this.shopMapper = shopMapper;
    }

    public Goods createGoods(Goods goods) {
        Shop shop = shopMapper.selectByPrimaryKey(goods.getShopId());
        if (Objects.equals(shop.getOwnerUserId(), UserContext.getCurrentUser().getId())) {
            long id = goodsMapper.insert(goods);
            goods.setId(id);
            return goods;
        }
        throw HttpException.forbidden("无权访问");
    }


    public Goods deleteGoodsById(Long goodsId) {
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        checkGoodsExist(goods);

        // Optional.ofNullable(goods.getShopId())
        //         .map(shopMapper::selectByPrimaryKey)
        //         .orElseThrow()
        if (Objects.equals(goods.getShopId(), UserContext.getCurrentUser().getId())) {
            goods.setStatus(StatusEnum.DELETE.toString());
            goodsMapper.updateByPrimaryKey(goods);
            return goods;
        }
        throw HttpException.forbidden("无权访问！");
    }

    public Goods updateGoods(Goods goods) {
        Goods goodsInDatabase = goodsMapper.selectByPrimaryKey(goods.getId());
        checkGoodsExist(goodsInDatabase);


        if (Objects.equals(goods.getShopId(), UserContext.getCurrentUser().getId())) {
            goods.setUpdatedAt(new Date());
            goodsMapper.updateByPrimaryKey(goods);
            return goods;
        }
        throw HttpException.forbidden("无权访问！");
    }

    private void checkGoodsExist(Goods goods) {
        if (goods == null) {
            throw HttpException.notFound("商品未找到！");
        }
    }

    private void checkOperationIsLegal(Goods goods) {

        // Optional.ofNullable(goods.getShopId())
        //         .map(shopMapper::selectByPrimaryKey)
        //         .filter(shop -> Objects.equals(shop.getOwnerUserId(), UserContext.getCurrentUser().getId()))
        //         .orElseThrow(shop -> HttpException.notFound("商品未找到！"));
        //

        if (Objects.equals(goods.getShopId(), UserContext.getCurrentUser().getId())) {
            throw HttpException.notFound("商品未找到！");
        }
    }
}
