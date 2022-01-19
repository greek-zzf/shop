package com.greek.shop.service;

import com.greek.shop.entity.Page;
import com.greek.shop.enums.StatusEnum;
import com.greek.shop.exception.HttpException;
import com.greek.shop.generate.Shop;
import com.greek.shop.generate.ShopExample;
import com.greek.shop.generate.ShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Zhaofeng Zhou
 * @date 21/11/2021 下午9:05
 */
@Service
public class ShopService {
    private final ShopMapper shopMapper;

    @Autowired
    public ShopService(ShopMapper shopMapper) {
        this.shopMapper = shopMapper;
    }

    public Page<Shop> getShopByUserId(Long userId, int pageNum, int pageSize) {
        ShopExample countByStatus = new ShopExample();
        countByStatus.createCriteria()
                .andStatusEqualTo(StatusEnum.OK.getName())
                .andOwnerUserIdEqualTo(userId);
        int totalNumber = (int) shopMapper.countByExample(countByStatus);
        int totalPage = totalNumber % pageSize == 0 ? totalNumber / pageSize : totalNumber / pageSize + 1;

        ShopExample pageCondition = new ShopExample();
        pageCondition.createCriteria()
                .andStatusEqualTo(StatusEnum.OK.getName())
                .andOwnerUserIdEqualTo(userId);
        pageCondition.setLimit(pageSize);
        pageCondition.setOffset((pageNum - 1) * pageSize);

        List<Shop> shopList = shopMapper.selectByExample(pageCondition);
        return Page.of(pageNum, pageSize, totalPage, shopList);
    }

    public Shop createShop(Shop shop, Long creatorId) {
        shop.setOwnerUserId(creatorId);
        shop.setUpdatedAt(new Date());
        shop.setCreatedAt(new Date());
        shopMapper.insert(shop);
        return shop;
    }

    public Shop updateShop(Shop shop) {
        Shop shopInDatabase = shopMapper.selectByPrimaryKey(shop.getId());
        checkShopExist(shopInDatabase);
        checkOperationIsLegal(shopInDatabase.getOwnerUserId());

        shop.setUpdatedAt(new Date());
        shopMapper.updateByPrimaryKeySelective(shop);
        return shop;
    }

    private void checkShopExist(Shop shop) {
        if (shop == null) {
            throw HttpException.notFound("店铺未找到！");
        }
    }

    private void checkOperationIsLegal(Long ownerId) {
        if (!Objects.equals(ownerId, UserContext.getCurrentUser().getId())) {
            throw HttpException.forbidden("无权访问！");
        }
    }

    public Shop deleteShop(Long shopId) {
        Shop shopInDatabase = shopMapper.selectByPrimaryKey(shopId);
        checkShopExist(shopInDatabase);
        checkOperationIsLegal(shopInDatabase.getOwnerUserId());

        shopInDatabase.setStatus(StatusEnum.DELETE.getName());
        shopMapper.updateByPrimaryKey(shopInDatabase);
        return shopInDatabase;

    }
}
