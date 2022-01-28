package com.greek.shop.service;

import com.greek.shop.api.enums.StatusEnum;
import com.greek.shop.dao.ShoppingCartQueryMapper;
import com.greek.shop.entity.AddToShoppingCartRequest;
import com.greek.shop.entity.GoodsWithNumber;
import com.greek.shop.api.data.Page;
import com.greek.shop.entity.ShoppingCartData;
import com.greek.shop.api.excepitons.HttpException;
import com.greek.shop.generate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * @author Zhaofeng Zhou
 * @date 2021/12/31 16:21
 */
@Service
public class ShoppingCartService {
    private ShoppingCartMapper shoppingCartMapper;
    private ShoppingCartQueryMapper shoppingCartQueryMapper;
    private GoodsMapper goodsMapper;
    private GoodsService goodsService;

    @Autowired
    ShoppingCartService(ShoppingCartMapper shoppingCartMapper,
                        ShoppingCartQueryMapper shoppingCartQueryMapper,
                        GoodsMapper goodsMapper,
                        GoodsService goodsService) {
        this.shoppingCartMapper = shoppingCartMapper;
        this.shoppingCartQueryMapper = shoppingCartQueryMapper;
        this.goodsMapper = goodsMapper;
        this.goodsService = goodsService;
    }

    public Page<ShoppingCartData> getShoppingCartOfUser(long userId, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        int totalNumber = shoppingCartQueryMapper.countShopInUserShoppingCart(UserContext.getCurrentUser().getId());
        List<ShoppingCartData> pageData = shoppingCartQueryMapper.selectShoppingCartDataByUserId(userId, offset, pageSize)
                .stream()
                .collect(groupingBy(shoppingCartData -> shoppingCartData.getShop().getId()))
                .values()
                .stream()
                .map(this::merge)
                .collect(toList());

        int totalPage = totalNumber % pageSize == 0 ? totalNumber / pageSize : totalNumber / pageSize + 1;
        return Page.of(pageNum, pageSize, totalPage, pageData);
    }

    private ShoppingCartData merge(List<ShoppingCartData> sameOfShop) {
        ShoppingCartData result = new ShoppingCartData();
        result.setShop(sameOfShop.get(0).getShop());
        List<GoodsWithNumber> goods = sameOfShop.stream()
                .map(ShoppingCartData::getGoods)
                .flatMap(List::stream)
                .collect(toList());
        result.setGoods(goods);
        return result;
    }

    public ShoppingCartData addToShoppingCart(AddToShoppingCartRequest request, long userId) {
        List<Long> goodsId = request.getGoods()
                .stream()
                .map(GoodsWithNumber::getId)
                .filter(Objects::nonNull)
                .collect(toList());

        if (goodsId.isEmpty()) {
            throw HttpException.badRequest("商品id为空!");
        }

        Map<Long, Goods> idToGoodsMap = goodsService.getIdToGoodsMap(goodsId);

        if (idToGoodsMap.values().stream().map(Goods::getShopId).collect(Collectors.toSet()).size() != 1) {
            throw HttpException.badRequest("商品id非法!");
        }


        List<ShoppingCart> shoppingCartRows = request.getGoods()
                .stream()
                .map(item -> toShoppingCart(item, idToGoodsMap))
                .collect(toList());

        shoppingCartRows.forEach(shoppingCartMapper::insert);
        return getLatestShoppingCartDataByUserIdShopId(new ArrayList<>(idToGoodsMap.values()).get(0).getShopId(), userId);
    }

    private ShoppingCartData getLatestShoppingCartDataByUserIdShopId(long shopId, long userId) {
        List<ShoppingCartData> resultRows = shoppingCartQueryMapper.selectShoppingCartDataByUserIdShopId(userId, shopId);
        return merge(resultRows);
    }


    private ShoppingCart toShoppingCart(GoodsWithNumber item, Map<Long, Goods> idToGoodsMap) {
        Goods goods = idToGoodsMap.get(item.getId());
        if (goods == null) {
            return null;
        }

        ShoppingCart result = new ShoppingCart();
        result.setNumber(item.getNumber());
        result.setGoodsId(item.getId());
        result.setUserId(UserContext.getCurrentUser().getId());
        result.setShopId(goods.getShopId());
        result.setStatus(StatusEnum.OK.toString().toLowerCase(Locale.ROOT));
        result.setCreatedAt(new Date());
        result.setUpdatedAt(new Date());
        return result;
    }

    public ShoppingCartData deleteGoodsInShoppingCart(long goodsId, long userId) {
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        if (goods == null) {
            throw HttpException.badRequest("商品未找到" + goodsId);
        }

        shoppingCartQueryMapper.deleteShoppingCart(goodsId, userId);
        return getLatestShoppingCartDataByUserIdShopId(goods.getShopId(), userId);
    }
}
