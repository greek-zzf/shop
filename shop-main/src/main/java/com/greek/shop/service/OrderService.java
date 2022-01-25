package com.greek.shop.service;

import com.greek.shop.api.data.GoodsInfo;
import com.greek.shop.api.data.OrderInfo;
import com.greek.shop.api.generate.Order;
import com.greek.shop.api.rpc.OrderRpcService;
import com.greek.shop.dao.GoodsStockMapper;
import com.greek.shop.entity.GoodsWithNumber;
import com.greek.shop.entity.OrderResponse;
import com.greek.shop.exception.HttpException;
import com.greek.shop.generate.Goods;
import com.greek.shop.generate.ShopMapper;
import com.greek.shop.generate.UserMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/20 16:29
 */
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @DubboReference(version = "${shop.orderservice.version}")
    OrderRpcService orderRpcService;

    private UserMapper userMapper;
    private GoodsService goodsService;
    private ShopMapper shopMapper;
    private SqlSessionFactory sqlSessionFactory;
    private GoodsStockMapper goodsStockMapper;

    @Autowired
    public OrderService(UserMapper userMapper,
                        GoodsStockMapper goodsStockMapper,
                        GoodsService goodsService,
                        ShopMapper shopMapper,
                        SqlSessionFactory sqlSessionFactory) {
        this.userMapper = userMapper;
        this.goodsStockMapper = goodsStockMapper;
        this.goodsService = goodsService;
        this.shopMapper = shopMapper;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public OrderResponse createOrder(OrderInfo orderInfo, long userId) {

        if (!deductStock(orderInfo)) {
            throw HttpException.gone("扣减库存失败");
        }

        Map<Long, Goods> idToGoodsMap = getIdToGoodsMap(orderInfo);
        Order createdOrder = createOrderViaRpc(orderInfo, userId, idToGoodsMap);
        return generateResponse(createdOrder, idToGoodsMap, orderInfo);
    }

    private GoodsWithNumber toGoodsWithNumber(GoodsInfo goodsInfo, Map<Long, Goods> idToGoodsMap) {
        GoodsWithNumber result = new GoodsWithNumber(idToGoodsMap.get(goodsInfo.getId()));
        result.setNumber(goodsInfo.getNumber());
        return result;
    }


    private BigDecimal calculateTotalPrice(OrderInfo orderInfo, Map<Long, Goods> idToGoodsMap) {
        BigDecimal result = BigDecimal.ZERO;

        for (GoodsInfo goodsInfo : orderInfo.getGoods()) {
            Goods goods = idToGoodsMap.get(goodsInfo.getId());
            if (goods == null) {
                throw HttpException.badRequest("goods id非法: " + goodsInfo.getId());
            }

            if (goodsInfo.getNumber() <= 0) {
                throw HttpException.badRequest("number非法: " + goodsInfo.getNumber());
            }

            result = result.add(goods.getPrice().multiply(new BigDecimal(goodsInfo.getNumber())));
        }

        return result;
    }

    /**
     * 扣减库存
     *
     * @param orderInfo
     * @return 扣减成功返回 true, 否则返回 false
     */
    private boolean deductStock(OrderInfo orderInfo) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(false)) {
            for (GoodsInfo goodsInfo : orderInfo.getGoods()) {
                if (goodsStockMapper.deductStock(goodsInfo) <= 0) {
                    logger.error("扣减库存失败, 商品id: {}, 数量: {}", goodsInfo.getId(), goodsInfo.getNumber());
                    sqlSession.rollback();
                    return false;
                }
            }
            sqlSession.commit();
            return true;
        }
    }

    private Map<Long, Goods> getIdToGoodsMap(OrderInfo orderInfo) {
        List<Long> goodsId = orderInfo.getGoods()
                .stream()
                .map(GoodsInfo::getId)
                .collect(toList());

        return goodsService.getIdToGoodsMap(goodsId);
    }

    private Order createOrderViaRpc(OrderInfo orderInfo, long userId, Map<Long, Goods> idToGoodsMap) {
        Order order = new Order();
        order.setUserId(userId);
        order.setAddress(userMapper.selectByPrimaryKey(userId).getAddress());
        order.setTotalPrice(calculateTotalPrice(orderInfo, idToGoodsMap));

        return orderRpcService.createOrder(orderInfo, order);
    }

    private OrderResponse generateResponse(Order createdOrder, Map<Long, Goods> idToGoodsMap, OrderInfo orderInfo) {

        OrderResponse response = new OrderResponse(createdOrder);

        Long shopId = new ArrayList<>(idToGoodsMap.values()).get(0).getShopId();
        response.setShop(shopMapper.selectByPrimaryKey(shopId));
        response.setGoods(
                orderInfo.getGoods()
                        .stream()
                        .map(goodsInfo -> toGoodsWithNumber(goodsInfo, idToGoodsMap))
                        .collect(toList()));

        return response;
    }
}
