package com.greek.shop.service;

import com.greek.shop.ShopApplication;
import com.greek.shop.entity.Page;
import com.greek.shop.entity.ShoppingCartData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/4 17:21
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartIntegration extends AbstractIntegrationTest {

    @Test
    void getShoppingCartPage() throws Exception {
        CookieAndUser cookieAndUser = loginAndReturnCookie();
        MvcResult result = getRequest("/api/v1/shoppingCart?pageNum=2&pageSize=1", cookieAndUser.getCookie(), status().isOk());
        Page<ShoppingCartData> pageData = asJsonObject(result);

        Assertions.assertEquals(2, pageData.getPageNum());
        Assertions.assertEquals(1, pageData.getPageSize());
        Assertions.assertEquals(3, pageData.getTotalPage());
        Assertions.assertEquals(1, pageData.getData().size());
        Assertions.assertEquals(2, pageData.getData().get(0).getShop().getId());


    }

}
