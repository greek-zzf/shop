package com.greek.shop.service;

import com.greek.shop.dao.GoodsMapper;
import com.greek.shop.dao.ShopMapper;
import com.greek.shop.entity.Goods;
import com.greek.shop.entity.Shop;
import com.greek.shop.enums.StatusEnum;
import com.greek.shop.exception.NotAuthorizedException;
import com.greek.shop.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        throw new NotAuthorizedException("无权访问");
    }


    public Goods deleteGoodsById(Long goodsId) {
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        if (goods == null) {
            throw new ResourceNotFoundException("商品未找到！");
        }
        goods.setStatus(StatusEnum.DELETE.toString());
        goodsMapper.updateByPrimaryKey(goods);
        return goods;
    }
}
