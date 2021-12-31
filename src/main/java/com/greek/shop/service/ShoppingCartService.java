package com.greek.shop.service;

import com.greek.shop.controller.ShoppingCartController;
import com.greek.shop.dao.ShoppingCartMapper;
import com.greek.shop.dao.ShoppingCartQueryMapper;
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
    ShoppingCartService(ShoppingCartMapper shoppingCartMapper) {
        this.shoppingCartMapper = shoppingCartMapper;
    }

    public List<ShoppingCartController.ShoppingCartData> getShoppingCart() {
        int count = shoppingCartQueryMapper.countShopInUserShoppingCart(UserContext.getCurrentUser().getId());
        return null;
    }

}
