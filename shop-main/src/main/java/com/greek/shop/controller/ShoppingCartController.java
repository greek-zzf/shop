package com.greek.shop.controller;

import com.greek.shop.entity.Page;
import com.greek.shop.entity.ShoppingCartData;
import com.greek.shop.entity.AddToShoppingCartRequest;
import com.greek.shop.service.ShoppingCartService;
import com.greek.shop.service.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zhaofeng Zhou
 * @date 2021/12/31 15:56
 */
@RestController
@RequestMapping("/api/v1")
public class ShoppingCartController {

    private ShoppingCartService shoppingCartService;

    @Autowired
    ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/shoppingCart")
    public Page<ShoppingCartData> getShoppingCart(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return shoppingCartService.getShoppingCartOfUser(UserContext.getCurrentUser().getId(), pageNum, pageSize);
    }

    @PostMapping("/shoppingCart")
    public ShoppingCartData addToShoppingCart(@RequestBody AddToShoppingCartRequest request) {
        return shoppingCartService.addToShoppingCart(request, UserContext.getCurrentUser().getId());
    }

    @DeleteMapping("/shoppingCart/{goodsId}")
    public ShoppingCartData deleteGoodsInShoppingCart(@PathVariable long goodsId) {
        return shoppingCartService.deleteGoodsInShoppingCart(goodsId, UserContext.getCurrentUser().getId());
    }

}
