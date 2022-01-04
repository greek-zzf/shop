package com.greek.shop.controller;

import com.greek.shop.entity.Page;
import com.greek.shop.entity.ShoppingCartData;
import com.greek.shop.service.ShoppingCartService;
import com.greek.shop.service.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhaofeng Zhou
 * @date 2021/12/31 15:56
 */
@RestController
public class ShoppingCartController {

    private ShoppingCartService shoppingCartService;

    @Autowired
    ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/shoppingCart")
    public Page<ShoppingCartData> getShoppingCart(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                  @RequestParam(value = "pageNum", defaultValue = "10") int pageSize) {
        return shoppingCartService.getShoppingCartOfUser(UserContext.getCurrentUser().getId(), pageNum, pageSize);
    }

}
