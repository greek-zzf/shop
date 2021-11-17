package com.greek.shop.service;

import com.greek.shop.ShopApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/15/015 17:17
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GoodsIntegrationTest {




    @Test
    public void testCreateGoods() {

    }

    @Test
    public void crateDeleteGoods() {

    }


}
