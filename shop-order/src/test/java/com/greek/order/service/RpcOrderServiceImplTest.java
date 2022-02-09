package com.greek.order.service;

import com.greek.order.mapper.OrderBatchMapper;
import com.greek.shop.api.data.GoodsInfo;
import com.greek.shop.api.data.OrderInfo;
import com.greek.shop.api.data.Page;
import com.greek.shop.api.data.RpcOrderGoods;
import com.greek.shop.api.enums.StatusEnum;
import com.greek.shop.api.excepitons.HttpException;
import com.greek.shop.api.generate.Order;
import com.greek.shop.api.generate.OrderGoodsMapper;
import com.greek.shop.api.generate.OrderMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Zhaofeng Zhou
 * @date 2022/2/9 9:22
 */

public class RpcOrderServiceImplTest {

    String databaseUrl = "jdbc:mysql://192.168.20.111:3307/test-order?useSSL=false&allowPublicKeyRetrieval=true";
    String username = "root";
    String password = "123456";

    RpcOrderServiceImpl rpcOrderService;

    SqlSession sqlSession;

    @BeforeEach
    void setUp() throws IOException {
        // 每次运行测试的时候，都需要将给测试数据库灌数据
        ClassicConfiguration conf = new ClassicConfiguration();
        conf.setDataSource(databaseUrl, username, password);
        Flyway flyway = new Flyway(conf);
        flyway.clean();
        flyway.migrate();

        // 读取 Mybatis 配置, 获取 SqlSession
        String resource = "test-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        sqlSession = sqlSessionFactory.openSession(true);

        // 构建 RpcOrderServiceImpl bean
        rpcOrderService = new RpcOrderServiceImpl(
                sqlSession.getMapper(OrderMapper.class),
                sqlSession.getMapper(OrderBatchMapper.class),
                sqlSession.getMapper(OrderGoodsMapper.class)
        );
    }

    @AfterEach
    void clean() {
        sqlSession.close();
    }

    @Test
    void createOrderTest() {
        OrderInfo orderInfo = new OrderInfo();
        GoodsInfo goods1 = new GoodsInfo(1, 2);
        GoodsInfo goods2 = new GoodsInfo(2, 10);
        orderInfo.setGoods(Arrays.asList(goods1, goods2));

        Order order = new Order();
        order.setUserId(1L);
        order.setShopId(1L);
        order.setAddress("火星");
        order.setTotalPrice(1000L);

        Order orderWithId = rpcOrderService.createOrder(orderInfo, order);

        Assertions.assertNotNull(orderWithId.getId());

        Order orderInDB = rpcOrderService.getOrderById(orderWithId.getId());

        Assertions.assertEquals(1L, orderInDB.getUserId());
        Assertions.assertEquals(1L, orderInDB.getShopId());
        Assertions.assertEquals("火星", orderInDB.getAddress());
        Assertions.assertEquals(1000L, orderInDB.getTotalPrice());
        Assertions.assertEquals(StatusEnum.PENDING.getName(), orderInDB.getStatus());
    }

    @Test
    void updateOrderTest() {
        Order order = rpcOrderService.getOrderById(2L);
        order.setExpressCompany("中通快递");
        order.setExpressId("ZT123456");
        order.setStatus(StatusEnum.DELIVERED.getName());

        RpcOrderGoods orderGoods = rpcOrderService.updateOrder(order);

        Assertions.assertEquals(StatusEnum.DELIVERED.getName(), order.getStatus());
        Assertions.assertEquals(700, orderGoods.getOrder().getTotalPrice());
        Assertions.assertEquals(2L, orderGoods.getOrder().getId());
        Assertions.assertEquals(1L, orderGoods.getOrder().getUserId());
        Assertions.assertEquals(1L, orderGoods.getOrder().getShopId());
        Assertions.assertEquals("火星", orderGoods.getOrder().getAddress());
        Assertions.assertEquals("中通快递", orderGoods.getOrder().getExpressCompany());
        Assertions.assertEquals("ZT123456", orderGoods.getOrder().getExpressId());

        List<GoodsInfo> goodsInfos = orderGoods.getGoods();
        Assertions.assertEquals(Arrays.asList(1L, 2L),
                goodsInfos.stream().map(GoodsInfo::getId).collect(toList()));
        Assertions.assertEquals(Arrays.asList(3, 4),
                goodsInfos.stream().map(GoodsInfo::getNumber).collect(toList()));

    }

    @Test
    void deleteOrderTest() {
        RpcOrderGoods rpcOrderGoods = rpcOrderService.deleteOrder(2L, 1L);

        Order order = rpcOrderGoods.getOrder();

        Assertions.assertEquals(StatusEnum.DELETE.getName(), order.getStatus());
        Assertions.assertEquals(700, order.getTotalPrice());
        Assertions.assertEquals(2L, order.getId());
        Assertions.assertEquals(1L, order.getUserId());
        Assertions.assertEquals(1L, order.getShopId());
        Assertions.assertEquals("火星", order.getAddress());

        List<GoodsInfo> goodsInfos = rpcOrderGoods.getGoods();
        Assertions.assertEquals(Arrays.asList(1L, 2L),
                goodsInfos.stream().map(GoodsInfo::getId).collect(toList()));
        Assertions.assertEquals(Arrays.asList(3, 4),
                goodsInfos.stream().map(GoodsInfo::getNumber).collect(toList()));


    }

    @Test
    void getOrderByPageTest() {

        Page<RpcOrderGoods> orderGoods = rpcOrderService.getOrder(1, 2, 1, null);


        Assertions.assertEquals(1, orderGoods.getPageSize());
        Assertions.assertEquals(2, orderGoods.getTotalPage());
        Assertions.assertEquals(2, orderGoods.getPageNum());
        Assertions.assertEquals(1, orderGoods.getData().size());

        Order order = orderGoods.getData().get(0).getOrder();

        Assertions.assertEquals(StatusEnum.PENDING.getName(), order.getStatus());
        Assertions.assertEquals(700, order.getTotalPrice());
        Assertions.assertEquals(2L, order.getId());
        Assertions.assertEquals(1L, order.getUserId());
        Assertions.assertEquals(1L, order.getShopId());
        Assertions.assertEquals("火星", order.getAddress());

        List<GoodsInfo> goodsInfos = orderGoods.getData().get(0).getGoods();
        Assertions.assertEquals(Arrays.asList(1L, 2L),
                goodsInfos.stream().map(GoodsInfo::getId).collect(toList()));
        Assertions.assertEquals(Arrays.asList(3, 4),
                goodsInfos.stream().map(GoodsInfo::getNumber).collect(toList()));
    }

    @Test
    void throwExceptionIfNotAuthorized() {
        HttpException httpException = Assertions.assertThrows(HttpException.class, () -> rpcOrderService.deleteOrder(2L, 2L));

        Assertions.assertEquals(403, httpException.getStatusCode());
    }

}
