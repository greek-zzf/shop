package com.greek.shop.service;

import com.greek.shop.api.data.GoodsInfo;
import com.greek.shop.api.data.OrderInfo;
import com.greek.shop.api.generate.Order;
import com.greek.shop.api.rpc.OrderRpcService;
import com.greek.shop.entity.GoodsWithNumber;
import com.greek.shop.entity.OrderResponse;
import com.greek.shop.exception.HttpException;
import com.greek.shop.generate.Goods;
import com.greek.shop.generate.GoodsMapper;
import com.greek.shop.generate.ShopMapper;
import com.greek.shop.generate.UserMapper;
import org.apache.dubbo.config.annotation.DubboReference;
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

    @DubboReference(version = "${shop.orderservice.version}")
    OrderRpcService orderRpcService;

    private UserMapper userMapper;
    private GoodsMapper goodsMapper;
    private GoodsService goodsService;
    private ShopMapper shopMapper;

    @Autowired
    public OrderService(UserMapper userMapper,
                        GoodsMapper goodsMapper,
                        GoodsService goodsService,
                        ShopMapper shopMapper) {
        this.userMapper = userMapper;
        this.goodsMapper = goodsMapper;
        this.goodsService = goodsService;
        this.shopMapper = shopMapper;
    }

    public OrderResponse createOrder(OrderInfo orderInfo, long userId) {
        List<Long> goodsId = orderInfo.getGoods()
                .stream()
                .map(GoodsInfo::getId)
                .collect(toList());

        Map<Long, Goods> idToGoodsMap = goodsService.getIdToGoodsMap(goodsId);

        Order order = new Order();
        order.setUserId(userId);
        order.setAddress(userMapper.selectByPrimaryKey(userId).getAddress());
        order.setTotalPrice(calculateTotalPrice(orderInfo, idToGoodsMap));

        Order createdOrder = orderRpcService.createOrder(orderInfo, order);

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
}
