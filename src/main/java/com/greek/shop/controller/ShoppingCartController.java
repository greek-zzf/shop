package com.greek.shop.controller;

import com.greek.shop.entity.Goods;
import com.greek.shop.entity.Shop;
import com.greek.shop.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

//    @PostMapping("/shoppingCart")
//    public void addToShoppingCart(@RequestBody AddToShoppingCartRequest request) {
//
//    }

    @GetMapping("/shoppingCart")
    public List<ShoppingCartData> getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    public static class ShoppingCartData {
        Shop shop;
        List<ShoppingCartGoods> goods;
    }

    public static class ShoppingCartGoods extends Goods {
        int number;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }


}
