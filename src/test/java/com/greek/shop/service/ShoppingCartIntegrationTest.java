package com.greek.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Sets;
import com.greek.shop.ShopApplication;
import com.greek.shop.entity.*;
import com.greek.shop.entity.vo.AddToShoppingCartRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Collections;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
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

    @Test
    void canAddShoppingCartData() throws Exception {
        CookieAndUser cookieAndUser = loginAndReturnCookie();

        AddToShoppingCartRequest request = new AddToShoppingCartRequest();
        ShoppingCartGoods item = new ShoppingCartGoods();
        item.setId(2L);
        item.setNumber(2);

        request.setGoods(Collections.singletonList(item));
        MvcResult response = postRequest("/api/v1/shoppingCart", cookieAndUser.getCookie(), request, status().isOk());
        Result<ShoppingCartData> result = asJsonObject(response, new TypeReference<Result<ShoppingCartData>>() {
        });

        Assertions.assertEquals(1, result.getData().getShop().getId());
        Assertions.assertEquals(Arrays.asList(1L, 2L), result.getData().getGoods().stream().map(Goods::getId).collect(toList()));
        Assertions.assertEquals(Sets.newHashSet(2, 100), result.getData().getGoods().stream().map(ShoppingCartGoods::getNumber).collect(toSet()));
        Assertions.assertTrue(result.getData().getGoods().stream().allMatch(goods -> goods.getShopId() == 1L));
    }


}
