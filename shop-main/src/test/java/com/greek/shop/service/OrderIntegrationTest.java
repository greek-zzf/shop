package com.greek.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.greek.shop.ShopApplication;
import com.greek.shop.api.data.GoodsInfo;
import com.greek.shop.api.data.OrderInfo;
import com.greek.shop.api.data.Page;
import com.greek.shop.api.data.RpcOrderGoods;
import com.greek.shop.api.enums.StatusEnum;
import com.greek.shop.api.generate.Order;
import com.greek.shop.entity.GoodsWithNumber;
import com.greek.shop.entity.OrderResponse;
import com.greek.shop.entity.Result;
import com.greek.shop.generate.Goods;
import com.greek.shop.generate.Shop;
import com.greek.shop.mock.MockOrderRpcService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static com.greek.shop.api.enums.StatusEnum.*;
import static java.util.stream.Collectors.toList;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
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
        when(mockOrderRpcService.orderRpcService.createOrder(any(), any()))
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

    @Test
    void canRollbackDeductStock() throws Exception {
        CookieAndUser cookieAndUser = loginAndReturnCookie();

        OrderInfo orderInfo = new OrderInfo();
        GoodsInfo goodsInfo1 = new GoodsInfo();
        GoodsInfo goodsInfo2 = new GoodsInfo();

        goodsInfo1.setNumber(3);
        goodsInfo1.setId(4);
        goodsInfo2.setNumber(6);
        goodsInfo2.setId(5);

        orderInfo.setGoods(Arrays.asList(goodsInfo1, goodsInfo2));

        postRequest("/api/v1/order", cookieAndUser.getCookie(), orderInfo, status().isGone());
    }


    @Test
    void canDeleteOrder() throws Exception {
        CookieAndUser cookieAndUser = loginAndReturnCookie();

        // 获取订单信息
        when(mockOrderRpcService.orderRpcService.getOrder(anyLong(), anyInt(), anyInt(), any()))
                .thenReturn(mockResponse());

        MvcResult request = getRequest("/api/v1/order?pageSize=2&pageNum=3", cookieAndUser.getCookie(), status().isOk());
        Page<OrderResponse> orderResponse = asJsonObject(request, new TypeReference<Page<OrderResponse>>() {
        });

        Assertions.assertEquals(3, orderResponse.getPageNum());
        Assertions.assertEquals(2, orderResponse.getPageSize());
        Assertions.assertEquals(10, orderResponse.getTotalPage());
        Assertions.assertEquals(Arrays.asList("shop2", "shop2"),
                orderResponse.getData().stream().map(OrderResponse::getShop).map(Shop::getName).collect(toList()));

        Assertions.assertEquals(Arrays.asList("goods3", "goods4"),
                orderResponse.getData().stream()
                        .map(OrderResponse::getGoods)
                        .flatMap(List::stream)
                        .map(Goods::getName)
                        .collect(toList()));

        Assertions.assertEquals(Arrays.asList(5, 3),
                orderResponse.getData().stream()
                        .map(OrderResponse::getGoods)
                        .flatMap(List::stream)
                        .map(GoodsWithNumber::getNumber)
                        .collect(toList()));

        // 删除订单
        when(mockOrderRpcService.orderRpcService.deleteOrder(100L, 1L))
                .thenReturn(mockRpcOrderGoods(100L, 1L, 3L, 2L, 5, DELETE));

        MvcResult mvcResult = deleteRequest("/api/v1/order/100", cookieAndUser.getCookie(), status().isOk());
        Result<OrderResponse> deleteOrderResponse = asJsonObject(mvcResult, new TypeReference<Result<OrderResponse>>() {
        });

        Assertions.assertEquals(100L, deleteOrderResponse.getData().getId());
        Assertions.assertEquals(1, deleteOrderResponse.getData().getGoods().size());
        Assertions.assertEquals(3L, deleteOrderResponse.getData().getGoods().get(0).getId());
        Assertions.assertEquals(5, deleteOrderResponse.getData().getGoods().get(0).getNumber());
    }

    @Test
    void return404IfNotFound() throws Exception {
        CookieAndUser cookieAndUser = loginAndReturnCookie();

        Order order = new Order();
        order.setId(123456L);

        MvcResult request = patchRequest("/api/v1/order", cookieAndUser.getCookie(), order, status().isNotFound());
    }

    @Test
    void canUpdateOrderExpressInformation() throws Exception {
        CookieAndUser cookieAndUser = loginAndReturnCookie();

        Order requestBody = new Order();
        requestBody.setId(123456L);
        requestBody.setShopId(2L);
        requestBody.setExpressCompany("顺丰");
        requestBody.setExpressId("SF123456789");


        Order orderInDB = new Order();
        orderInDB.setId(123456L);
        orderInDB.setShopId(2L);
        // 获取订单信息
        when(mockOrderRpcService.orderRpcService.getOrderById(123456L))
                .thenReturn(orderInDB);
        when(mockOrderRpcService.orderRpcService.updateOrder(any())).thenReturn(
                mockRpcOrderGoods(123456L, 1L, 3L, 2L, 10, DELIVERED));

        MvcResult request = patchRequest("/api/v1/order", cookieAndUser.getCookie(), requestBody, status().isOk());
        Result<OrderResponse> orderResponse = asJsonObject(request, new TypeReference<Result<OrderResponse>>() {
        });

        Assertions.assertEquals(2L, orderResponse.getData().getShop().getId());
        Assertions.assertEquals("shop2", orderResponse.getData().getShop().getName());
        Assertions.assertEquals(DELIVERED.getName(), orderResponse.getData().getStatus());
        Assertions.assertEquals(1, orderResponse.getData().getGoods().size());
        Assertions.assertEquals(3, orderResponse.getData().getGoods().get(0).getId());
        Assertions.assertEquals(10, orderResponse.getData().getGoods().get(0).getNumber());
    }

    @Test
    void canUpdateOrderStatus() throws Exception {
        CookieAndUser cookieAndUser = loginAndReturnCookie();

        Order requestBody = new Order();
        requestBody.setId(123456L);
        requestBody.setStatus(RECEIVED.getName());


        Order orderInDB = new Order();
        orderInDB.setId(123456L);
        orderInDB.setUserId(1L);
        // 获取订单信息
        when(mockOrderRpcService.orderRpcService.getOrderById(123456L))
                .thenReturn(orderInDB);
        when(mockOrderRpcService.orderRpcService.updateOrder(any())).thenReturn(
                mockRpcOrderGoods(123456L, 1L, 3L, 2L, 10, RECEIVED));

        MvcResult request = patchRequest("/api/v1/order", cookieAndUser.getCookie(), requestBody, status().isOk());
        Result<OrderResponse> orderResponse = asJsonObject(request, new TypeReference<Result<OrderResponse>>() {
        });

        Assertions.assertEquals(2L, orderResponse.getData().getShop().getId());
        Assertions.assertEquals("shop2", orderResponse.getData().getShop().getName());
        Assertions.assertEquals(RECEIVED.getName(), orderResponse.getData().getStatus());
        Assertions.assertEquals(1, orderResponse.getData().getGoods().size());
        Assertions.assertEquals(3, orderResponse.getData().getGoods().get(0).getId());
        Assertions.assertEquals(10, orderResponse.getData().getGoods().get(0).getNumber());
    }


    private Page<RpcOrderGoods> mockResponse() {
        RpcOrderGoods order1 = mockRpcOrderGoods(100, 1, 3, 2, 5, DELIVERED);
        RpcOrderGoods order2 = mockRpcOrderGoods(101, 1, 4, 2, 3, RECEIVED);

        return Page.of(3, 2, 10, Arrays.asList(order1, order2));
    }


    private RpcOrderGoods mockRpcOrderGoods(long orderId,
                                            long userId,
                                            long goodsId,
                                            long shopId,
                                            int number,
                                            StatusEnum statusEnum) {

        Order order = new Order();
        order.setId(orderId);
        order.setUserId(userId);
        order.setShopId(shopId);
        order.setStatus(statusEnum.getName());

        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.setId(goodsId);
        goodsInfo.setNumber(number);

        RpcOrderGoods orderGoods = new RpcOrderGoods();
        orderGoods.setGoods(Arrays.asList(goodsInfo));
        orderGoods.setOrder(order);
        return orderGoods;
    }
}
