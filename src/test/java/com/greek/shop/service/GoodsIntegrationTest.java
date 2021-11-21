package com.greek.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.greek.shop.ShopApplication;
import com.greek.shop.entity.Goods;
import com.greek.shop.entity.Result;
import com.greek.shop.entity.Shop;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/15/015 17:17
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GoodsIntegrationTest extends AbstractIntegrationTest {

    @Test
    public void testCreateGoods() throws Exception {
        CookieAndUser cookieAndUser = loginAndReturnCookie();

        Shop shop = new Shop();
        shop.setName("店铺1");
        shop.setDescription("店铺描述1");
        shop.setImgUrl("http://shopurl");

        MvcResult result = postRequest("/api/v1/shop", cookieAndUser.getCookie(), shop, status().isCreated());
        Result<Shop> shopResult = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<Result<Shop>>() {
        });
        assertEquals("店铺1", shopResult.getData().getName());
        assertEquals("店铺描述1", shopResult.getData().getDescription());
        assertEquals("http://shopurl", shopResult.getData().getImgUrl());
        assertEquals(cookieAndUser.getUser().getId(), shopResult.getData().getOwnerUserId());

        Goods goods = new Goods();
        goods.setName("肥皂");
        goods.setDescription("纯天然无污染肥皂");
        goods.setDetails("这是一块好肥皂");
        goods.setImgUrl("https://img.url");
        goods.setPrice(500L);
        goods.setStock(10);
        goods.setShopId(shopResult.getData().getId());

        result = postRequest("/api/v1/goods", cookieAndUser.getCookie(), goods, status().isCreated());
        Result<Goods> response = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<Result<Goods>>() {
        });
        assertEquals(response.getData().getName(), "肥皂");
        assertEquals(response.getData().getShopId(), shopResult.getData().getId());
        assertEquals("ok", response.getData().getStatus());
    }

    @Test
    public void crateDeleteGoods() {

    }


}
