package com.greek.shop.service;

import com.greek.shop.entity.Page;
import com.greek.shop.enums.StatusEnum;
import com.greek.shop.exception.HttpException;
import com.greek.shop.generate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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

        goods.setStatus(StatusEnum.OK.getName());
        goodsMapper.insert(goods);
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
        Shop shop = shopMapper.selectByPrimaryKey(goods.getShopId());
        checkOperationIsLegal(shop.getOwnerUserId());

        goods.setUpdatedAt(new Date());
        int updateRows = goodsMapper.updateByPrimaryKey(goods);
        if (updateRows == 0) {
            throw HttpException.notFound("商品未找到！");
        }
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

    public Page<Goods> getGoodsPage(int pageNum, int pageSize, Long shopId) {
        int totalNumber = countGoods(shopId);
        int totalPage = totalNumber % pageSize == 0 ? totalNumber / pageSize : totalNumber / pageSize + 1;

        GoodsExample page = new GoodsExample();
        page.setLimit(pageSize);
        page.setOffset((pageNum - 1) * pageSize);

        List<Goods> goodsList = goodsMapper.selectByExample(page);
        return Page.of(pageNum, pageSize, totalPage, goodsList);
    }

    private int countGoods(Long shopId) {

        GoodsExample example = new GoodsExample();
        if (shopId == null) {
            example.createCriteria().andStatusEqualTo(StatusEnum.OK.getName());
        } else {
            example.createCriteria()
                    .andStatusEqualTo(StatusEnum.OK.getName())
                    .andShopIdEqualTo(shopId);
        }
        return (int) goodsMapper.countByExample(example);
    }
}
