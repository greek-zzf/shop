package com.greek.shop.service;

import com.greek.shop.dao.ShoppingCartMapper;
import com.greek.shop.dao.ShoppingCartQueryMapper;
import com.greek.shop.entity.Page;
import com.greek.shop.entity.ShoppingCartData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zhaofeng Zhou
 * @date 2021/12/31 16:21
 */
@Service
public class ShoppingCartService {
    private ShoppingCartMapper shoppingCartMapper;
    private ShoppingCartQueryMapper shoppingCartQueryMapper;

    @Autowired
    ShoppingCartService(ShoppingCartMapper shoppingCartMapper, ShoppingCartQueryMapper shoppingCartQueryMapper) {
        this.shoppingCartMapper = shoppingCartMapper;
        this.shoppingCartQueryMapper = shoppingCartQueryMapper;
    }

    public Page<ShoppingCartData> getShoppingCartOfUser(long userId, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        int totalNumber = shoppingCartQueryMapper.countShopInUserShoppingCart(UserContext.getCurrentUser().getId());
        List<ShoppingCartData> pageData = shoppingCartQueryMapper.selectShoppingCartDataByUserId(userId, offset, pageSize);

        int totalPage = totalNumber % pageSize == 0 ? totalNumber / pageSize : totalNumber / pageSize + 1;
        return Page.of(pageNum, pageSize, totalPage, pageData);
    }

}
