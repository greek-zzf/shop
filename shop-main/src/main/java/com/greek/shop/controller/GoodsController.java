package com.greek.shop.controller;

import com.greek.shop.entity.Page;
import com.greek.shop.generate.Goods;
import com.greek.shop.service.GoodsService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping("/goods")
    public Page<Goods> getGoods(@RequestParam("pageNum") int pageNum,
                                @RequestParam("pageSize") int pageSize,
                                @RequestParam(value = "shopId", required = false) Long shopId) {
        return goodsService.getGoodsPage(pageNum, pageSize, shopId);
    }


    @PostMapping("/goods")
    @ResponseStatus(HttpStatus.CREATED)
    public Goods createGoods(@RequestBody @Valid Goods goods) {
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

    @PatchMapping("/goods")
    public Goods updateGoods(@RequestBody @Valid Goods goods) {
        return goodsService.updateGoods(goods);
    }
}
