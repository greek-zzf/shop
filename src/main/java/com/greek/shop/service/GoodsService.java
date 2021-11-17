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
        checkOperationIsLegal(shop.getOwnerUserId());

        long id = goodsMapper.insert(goods);
        goods.setId(id);
        return goods;
    }


    public Goods deleteGoodsById(Long goodsId) {
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        checkGoodsExist(goods);

        Shop shop = shopMapper.selectByPrimaryKey(goods.getShopId());
        checkOperationIsLegal(shop.getOwnerUserId());
        goods.setStatus(StatusEnum.DELETE.toString());
        goodsMapper.updateByPrimaryKey(goods);
        return goods;
    }

    public Goods updateGoods(Goods goods) {
        Goods goodsInDatabase = goodsMapper.selectByPrimaryKey(goods.getId());

        checkGoodsExist(goodsInDatabase);

        Shop shop = shopMapper.selectByPrimaryKey(goods.getShopId());
        checkOperationIsLegal(shop.getOwnerUserId());

        goods.setUpdatedAt(new Date());
        goodsMapper.updateByPrimaryKey(goods);
        return goods;
    }

    private void checkGoodsExist(Goods goods) {
        if (goods == null) {
            throw HttpException.notFound("商品未找到！");
        }
    }

    private void checkOperationIsLegal(Long ownerId) {
        if (!Objects.equals(ownerId, UserContext.getCurrentUser().getId())) {
            throw HttpException.forbidden("无权访问！");
        }
    }
}
