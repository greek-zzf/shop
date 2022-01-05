package com.greek.shop.service;

import com.greek.shop.dao.GoodsMapper;
import com.greek.shop.dao.ShoppingCartMapper;
import com.greek.shop.dao.ShoppingCartQueryMapper;
import com.greek.shop.entity.*;
import com.greek.shop.entity.vo.AddToShoppingCartRequest;
import com.greek.shop.enums.StatusEnum;
import com.greek.shop.exception.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * @author Zhaofeng Zhou
 * @date 2021/12/31 16:21
 */
@Service
public class ShoppingCartService {
    private ShoppingCartMapper shoppingCartMapper;
    private ShoppingCartQueryMapper shoppingCartQueryMapper;
    private GoodsMapper goodsMapper;

    @Autowired
    ShoppingCartService(ShoppingCartMapper shoppingCartMapper,
                        ShoppingCartQueryMapper shoppingCartQueryMapper,
                        GoodsMapper goodsMapper) {
        this.shoppingCartMapper = shoppingCartMapper;
        this.shoppingCartQueryMapper = shoppingCartQueryMapper;
        this.goodsMapper = goodsMapper;
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
        List<ShoppingCartGoods> goods = sameOfShop.stream()
                .map(ShoppingCartData::getGoods)
                .flatMap(List::stream)
                .collect(toList());
        result.setGoods(goods);
        return result;
    }

    public ShoppingCartData addToShoppingCart(AddToShoppingCartRequest request) {
        List<Long> goodsId = request.getGoods()
                .stream()
                .map(ShoppingCartGoods::getId)
                .collect(toList());

        if (goodsId.isEmpty()) {
            throw HttpException.badRequest("商品id为空!");
        }

        GoodsExample example = new GoodsExample();
        example.createCriteria().andIdIn(goodsId);
        List<Goods> goods = goodsMapper.selectByExample(example);

        if (goods.stream().map(Goods::getShopId).collect(Collectors.toSet()).size() != 1) {
            throw HttpException.badRequest("商品id非法!");
        }

        Map<Long, Goods> idToGoodsMap = goods.stream().collect(toMap(Goods::getId, Function.identity()));

        List<ShoppingCart> shoppingCartRows = request.getGoods()
                .stream()
                .map(item -> toShoppingCart(item, idToGoodsMap))
                .collect(toList());

        shoppingCartRows.forEach(shoppingCartMapper::insert);

        return merge(shoppingCartQueryMapper.selectShoppingCartDataByUserIdShopId(
                UserContext.getCurrentUser().getId(),
                goods.get(0).getShopId()));
    }


    private ShoppingCart toShoppingCart(ShoppingCartGoods item, Map<Long, Goods> idToGoodsMap) {
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
}
