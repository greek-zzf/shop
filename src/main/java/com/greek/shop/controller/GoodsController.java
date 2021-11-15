package com.greek.shop.controller;

import com.greek.shop.entity.Goods;
import com.greek.shop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/15/015 14:50
 */
@RestController
@RequestMapping("/api/v1")
public class GoodsController {

    private final GoodsService goodsService;

    @Autowired
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @PostMapping("/goods")
    @ResponseStatus(HttpStatus.CREATED)
    public Goods createGoods(@RequestBody Goods goods) {
        clean(goods);
        return goodsService.createGoods(goods);
    }

    private void clean(Goods goods) {
        goods.setId(null);
        goods.setCreatedAt(new Date());
        goods.setUpdatedAt(new Date());
    }

    @DeleteMapping("/goods/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Goods deleteGoods(@PathVariable("id") Long goodsId) {
        return goodsService.deleteGoodsById(goodsId);
    }
}
