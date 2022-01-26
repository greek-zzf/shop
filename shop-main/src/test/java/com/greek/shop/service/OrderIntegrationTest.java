package com.greek.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.greek.shop.ShopApplication;
import com.greek.shop.api.data.GoodsInfo;
import com.greek.shop.api.data.OrderInfo;
import com.greek.shop.api.generate.Order;
import com.greek.shop.entity.GoodsWithNumber;
import com.greek.shop.entity.OrderResponse;
import com.greek.shop.entity.Result;
import com.greek.shop.generate.Goods;
import com.greek.shop.mock.MockOrderRpcService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static com.greek.shop.api.enums.StatusEnum.PENDING;
import static java.util.stream.Collectors.toList;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/26 14:06
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    MockOrderRpcService mockOrderRpcService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(mockOrderRpcService);
        Mockito.when(mockOrderRpcService.orderRpcService.createOrder(any(), any()))
                .thenAnswer(invocation -> {
                    Order order = invocation.getArgument(1);
                    order.setId(1234L);
                    return order;
                });
    }

    @Test
    void canCreateOrder() throws Exception {
        CookieAndUser cookieAndUser = loginAndReturnCookie();

        OrderInfo orderInfo = new OrderInfo();
        GoodsInfo goodsInfo1 = new GoodsInfo();
        GoodsInfo goodsInfo2 = new GoodsInfo();

        goodsInfo1.setNumber(3);
        goodsInfo1.setId(4);
        goodsInfo2.setNumber(5);
        goodsInfo2.setId(5);

        orderInfo.setGoods(Arrays.asList(goodsInfo1, goodsInfo2));

        MvcResult mvcResult = postRequest("/api/v1/order", cookieAndUser.getCookie(), orderInfo, status().isOk());

        Result<OrderResponse> response = asJsonObject(mvcResult, new TypeReference<Result<OrderResponse>>() {
        });

        Assertions.assertEquals(1234L, response.getData().getId());
        Assertions.assertEquals("shop2", response.getData().getShop().getName());
        Assertions.assertEquals(PENDING.getName(), response.getData().getStatus());
        Assertions.assertEquals("火星", response.getData().getAddress());
        Assertions.assertEquals(Arrays.asList(4L, 5L),
                response.getData().getGoods().stream().map(Goods::getId).collect(toList()));

        Assertions.assertEquals(Arrays.asList(3, 5),
                response.getData().getGoods().stream().map(GoodsWithNumber::getNumber).collect(toList()));
    }
}
