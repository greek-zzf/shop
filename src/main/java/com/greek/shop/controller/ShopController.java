package com.greek.shop.controller;

import com.greek.shop.entity.Page;
import com.greek.shop.entity.Shop;
import com.greek.shop.service.ShopService;
import com.greek.shop.service.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zhaofeng Zhou
 * @date 21/11/2021 下午9:05
 */
@RestController
@RequestMapping("/api/v1")
public class ShopController {

    private final ShopService shopService;

    @Autowired
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/shop")
    public Page<Shop> getShops(@RequestParam("pageNum") int pageNum,
                               @RequestParam("pageSize") int pageSize,
                               @RequestParam(value = "shopId", required = false) Long shopId) {
        return shopService.getShopByUserId(UserContext.getCurrentUser().getId(), pageNum, pageSize);

    }

    @PostMapping("/shop")
    @ResponseStatus(HttpStatus.CREATED)
    public Shop createShop(@RequestBody Shop shop) {
        return shopService.createShop(shop, UserContext.getCurrentUser().getId());
    }

    @PatchMapping("/shop/{id}")
    public Shop updateShop(@PathVariable("id") Long id, @RequestBody Shop shop) {
        shop.setId(id);
        return shopService.updateShop(shop);
    }

    @DeleteMapping("/shop/{id}")
    public Shop deleteShop(@PathVariable("id") Long shopId) {
        return shopService.deleteShop(shopId);
    }


}
