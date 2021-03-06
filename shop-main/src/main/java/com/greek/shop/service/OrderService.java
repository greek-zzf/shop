package com.greek.shop.service;

import com.greek.shop.api.data.GoodsInfo;
import com.greek.shop.api.data.OrderInfo;
import com.greek.shop.api.data.Page;
import com.greek.shop.api.data.RpcOrderGoods;
import com.greek.shop.api.enums.StatusEnum;
import com.greek.shop.api.excepitons.HttpException;
import com.greek.shop.api.generate.Order;
import com.greek.shop.api.rpc.OrderRpcService;
import com.greek.shop.dao.GoodsStockMapper;
import com.greek.shop.entity.GoodsWithNumber;
import com.greek.shop.entity.OrderResponse;
import com.greek.shop.generate.Goods;
import com.greek.shop.generate.Shop;
import com.greek.shop.generate.ShopMapper;
import com.greek.shop.generate.UserMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.greek.shop.api.enums.StatusEnum.PENDING;
import static java.util.stream.Collectors.toList;

/**
 * @author Zhaofeng Zhou
 * @date 2022/1/20 16:29
 */
@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @DubboReference(version = "${shop.orderservice.version}", url = "${shop.orderservice.url}")
    OrderRpcService orderRpcService;

    private UserMapper userMapper;
    private GoodsService goodsService;
    private ShopMapper shopMapper;
    private GoodsStockMapper goodsStockMapper;

    @Autowired
    public OrderService(UserMapper userMapper,
                        GoodsStockMapper goodsStockMapper,
                        GoodsService goodsService,
                        ShopMapper shopMapper) {
        this.userMapper = userMapper;
        this.goodsStockMapper = goodsStockMapper;
        this.goodsService = goodsService;
        this.shopMapper = shopMapper;
    }

    public OrderResponse createOrder(OrderInfo orderInfo, long userId) {

        Map<Long, Goods> idToGoodsMap = getIdToGoodsMap(orderInfo.getGoods());
        Order createdOrder = createOrderViaRpc(orderInfo, userId, idToGoodsMap);
        return generateResponse(createdOrder, idToGoodsMap, orderInfo.getGoods());
    }

    private GoodsWithNumber toGoodsWithNumber(GoodsInfo goodsInfo, Map<Long, Goods> idToGoodsMap) {
        GoodsWithNumber result = new GoodsWithNumber(idToGoodsMap.get(goodsInfo.getId()));
        result.setNumber(goodsInfo.getNumber());
        return result;
    }


    private long calculateTotalPrice(OrderInfo orderInfo, Map<Long, Goods> idToGoodsMap) {
        long result = 0;

        for (GoodsInfo goodsInfo : orderInfo.getGoods()) {
            Goods goods = idToGoodsMap.get(goodsInfo.getId());
            if (goods == null) {
                throw HttpException.badRequest("goods id??????: " + goodsInfo.getId());
            }

            if (goodsInfo.getNumber() <= 0) {
                throw HttpException.badRequest("number??????: " + goodsInfo.getNumber());
            }

            result = result + goods.getPrice() * goodsInfo.getNumber();
        }

        return result;
    }

    /**
     * ????????????
     *
     * @param orderInfo
     */
    @Transactional(rollbackFor = HttpException.class)
    public void deductStock(OrderInfo orderInfo) {
        for (GoodsInfo goodsInfo : orderInfo.getGoods()) {
            if (goodsStockMapper.deductStock(goodsInfo) <= 0) {
                logger.error("??????????????????, ??????id: {}, ??????: {}", goodsInfo.getId(), goodsInfo.getNumber());
                throw HttpException.gone("??????????????????");
            }
        }
    }

    private Map<Long, Goods> getIdToGoodsMap(List<GoodsInfo> goodsInfos) {
        List<Long> goodsId = goodsInfos
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
        order.setStatus(PENDING.getName());

        return orderRpcService.createOrder(orderInfo, order);
    }

    private OrderResponse generateResponse(Order createdOrder, Map<Long, Goods> idToGoodsMap, List<GoodsInfo> goodsInfos) {

        OrderResponse response = new OrderResponse(createdOrder);

        Long shopId = new ArrayList<>(idToGoodsMap.values()).get(0).getShopId();
        response.setShop(shopMapper.selectByPrimaryKey(shopId));
        response.setGoods(
                goodsInfos
                        .stream()
                        .map(goodsInfo -> toGoodsWithNumber(goodsInfo, idToGoodsMap))
                        .collect(toList()));

        return response;
    }

    public OrderResponse deleteOrder(long orderId, long userId) {
        return toOrderResponse(orderRpcService.deleteOrder(orderId, userId));
    }

    public Page<OrderResponse> getOrder(long userId, Integer pageNum, Integer pageSize, StatusEnum status) {
        Page<RpcOrderGoods> rpcOrderGoods = orderRpcService.getOrder(userId, pageNum, pageSize, status);

        List<GoodsInfo> goodIds = rpcOrderGoods.getData()
                .stream()
                .map(RpcOrderGoods::getGoods)
                .flatMap(List::stream)
                .collect(toList());

        Map<Long, Goods> idToGoodsMap = getIdToGoodsMap(goodIds);

        List<OrderResponse> orders = rpcOrderGoods.getData()
                .stream()
                .map(order -> generateResponse(order.getOrder(), idToGoodsMap, order.getGoods()))
                .collect(toList());

        return Page.of(rpcOrderGoods.getPageNum(), rpcOrderGoods.getPageSize(), rpcOrderGoods.getTotalPage(), orders);
    }

    public OrderResponse updateExpressInformation(Order order, long userId) {
        Order orderInDatabase = orderRpcService.getOrderById(order.getId());
        if (orderInDatabase == null) {
            throw HttpException.notFound("???????????????: " + order.getId());
        }

        Shop shop = shopMapper.selectByPrimaryKey(orderInDatabase.getShopId());
        if (shop == null) {
            throw HttpException.notFound("???????????????: " + orderInDatabase.getShopId());
        }

        if (shop.getOwnerUserId() != userId) {
            throw HttpException.forbidden("????????????! ");
        }

        Order copy = new Order();
        copy.setId(order.getId());
        copy.setExpressId(order.getExpressId());
        copy.setExpressCompany(order.getExpressCompany());
        return toOrderResponse(orderRpcService.updateOrder(copy));
    }

    public OrderResponse updateOrderStatus(Order order, long userId) {
        Order orderInDatabase = orderRpcService.getOrderById(order.getId());
        if (orderInDatabase == null) {
            throw HttpException.notFound("???????????????: " + order.getId());
        }

        if (orderInDatabase.getUserId() != userId) {
            throw HttpException.forbidden("????????????! ");
        }

        Order copy = new Order();
        copy.setId(order.getId());
        copy.setStatus(order.getStatus());
        return toOrderResponse(orderRpcService.updateOrder(copy));
    }

    private OrderResponse toOrderResponse(RpcOrderGoods rpcOrderGoods) {
        Map<Long, Goods> idToGoodsMap = getIdToGoodsMap(rpcOrderGoods.getGoods());
        return generateResponse(rpcOrderGoods.getOrder(), idToGoodsMap, rpcOrderGoods.getGoods());
    }
}
