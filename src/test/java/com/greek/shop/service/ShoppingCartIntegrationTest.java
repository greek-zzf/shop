package com.greek.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.greek.shop.ShopApplication;
import com.greek.shop.entity.Goods;
import com.greek.shop.entity.Page;
import com.greek.shop.entity.ShoppingCartData;
import com.greek.shop.entity.ShoppingCartGoods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static java.util.stream.Collectors.toList;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/4 17:21
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartIntegrationTest extends AbstractIntegrationTest {

    @Test
    void getShoppingCartPage() throws Exception {
        CookieAndUser cookieAndUser = loginAndReturnCookie();
        MvcResult result = getRequest("/api/v1/shoppingCart?pageNum=2&pageSize=1", cookieAndUser.getCookie(), status().isOk());
        Page<ShoppingCartData> pageData = asJsonObject(result, new TypeReference<Page<ShoppingCartData>>() {
        });

        Assertions.assertEquals(2, pageData.getPageNum());
        Assertions.assertEquals(1, pageData.getPageSize());
        Assertions.assertEquals(2, pageData.getTotalPage());
        Assertions.assertEquals(1, pageData.getData().size());
        Assertions.assertEquals(2, pageData.getData().get(0).getShop().getId());

        Assertions.assertEquals(Arrays.asList(4L, 5L),
                pageData.getData().get(0).getGoods()
                        .stream()
                        .map(Goods::getId)
                        .collect(toList()));

        Assertions.assertEquals(Arrays.asList(200, 300),
                pageData.getData().get(0).getGoods()
                        .stream()
                        .map(ShoppingCartGoods::getNumber)
                        .collect(toList()));

        Assertions.assertEquals(Arrays.asList(100L, 200L),
                pageData.getData().get(0).getGoods()
                        .stream()
                        .map(ShoppingCartGoods::getPrice)
                        .collect(toList()));

    }

}
