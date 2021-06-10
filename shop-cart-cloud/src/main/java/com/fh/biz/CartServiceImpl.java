package com.fh.biz;

import com.alibaba.fastjson.JSON;
import com.fh.goods.OrderService;
import com.fh.mapper.MemberMapper;
import com.fh.mapper.SkuMapper;
import com.fh.mapper.SpuMapper;
import com.fh.shop.common.Attestation;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.common.SysCont;
import com.fh.shop.goods.po.Sku;
import com.fh.shop.goods.po.Spu;
import com.fh.shop.goods.vo.CartItemVo;
import com.fh.shop.util.BigDecimalUtil;
import com.fh.shop.util.RedisUtil;
import com.fh.vo.CartVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service("cartService")
@Transactional(rollbackFor = Exception.class)
public class CartServiceImpl implements CartService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SpuMapper spuMapper;

    @Value("${cart.restriction}")
    private Long restriction;

    @Override
    public ServerResponse addCart(Long memberId, Long count, Long skuId) {
        // 商品是否存在
        ServerResponse<Sku> skuResponse = orderService.findSku(skuId);
        Sku sku = skuResponse.getData();
//        Sku sku = skuMapper.selectById(skuId);
        if (sku == null){
            return ServerResponse.error(ResponseEnum.CART_MEMBER_IS_NOT);
        }
        // 商品是否上架
        Spu spu = spuMapper.selectById(sku.getSpuId());
        if (spu.getStatues().equals(SysCont.TWO)) {
            return ServerResponse.error(ResponseEnum.CART_STATUS_IS_NOT);
        }
        // 商品的库存量大于等于购买的数量
        Integer stock = sku.getStock();
        if (stock.intValue() < count){
            return ServerResponse.error(ResponseEnum.CART_STOCK_IS_NOT);
        }
        // 会员是否有购物车
        String key = Attestation.builderCartKey(memberId);
        String cartJson = RedisUtil.hget(key,SysCont.CARTJSON);
        // 如果没有购物车
        if (StringUtils.isEmpty(cartJson)) {
            // 创建一个购物车，直接把商品加入到购物车
            if (count>restriction){
                return ServerResponse.error(ResponseEnum.CART_BUY_IS_NOT);
            }
            CartVo cartVo = new CartVo();
            CartItemVo cartItemVo = new CartItemVo();
            cartItemVo.setCount(count);
            String price = sku.getPrice().toString();
            cartItemVo.setPrice(price);
            cartItemVo.setSkuId(sku.getId());
            cartItemVo.setSkuImage(sku.getImage());
            cartItemVo.setSkuName(sku.getSkuName());
            BigDecimal subPrice = BigDecimalUtil.multiply(price,count+"");
            cartItemVo.setSubPrice(subPrice.toString());
            cartVo.getCartItemVoList().add(cartItemVo);
            cartVo.setTotalCount(count);
            cartVo.setTotalPrice(cartItemVo.getSubPrice());
            RedisUtil.hset(key,SysCont.CARTJSON,JSON.toJSONString(cartVo));
            return ServerResponse.sucess();
        } else {
            // 如果有购物车
            CartVo cartVo = JSON.parseObject(cartJson, CartVo.class);
            List<CartItemVo> cartItemVoList = cartVo.getCartItemVoList();
            Optional<CartItemVo> item = cartItemVoList.stream().filter(x -> x.getSkuId().longValue() == skuId.longValue()).findFirst();
            if (item.isPresent()) {
                // 购物车有这款商品，找到这款商品，更新商品的数量，小计
                CartItemVo cartItemVo = item.get();
                long count1 = cartItemVo.getCount() + count;
                if (count1>restriction){
                    return ServerResponse.error(ResponseEnum.CART_BUY_IS_NOT);
                }
                cartItemVo.setCount(count1);
              // 判断是否数据小于0
                if (count1<=SysCont.ZERO){
                    cartItemVoList.removeIf(x -> x.getSkuId().longValue()==skuId);
                    updateCart(key, cartVo);
                    return ServerResponse.sucess();
                }
//                如果没有数据的话，那就清除购物车
                if (cartItemVoList.size()==0){
                   RedisUtil.delete(key);
                   return ServerResponse.sucess(ResponseEnum.CART_CART_NOT);
                }
                BigDecimal subPrice = new BigDecimal(cartItemVo.getSubPrice());
                String subPriceStr = subPrice.add(BigDecimalUtil.multiply(cartItemVo.getPrice(), count + "")).toString();
                cartItemVo.setSubPrice(subPriceStr);
                //更新购物车
                updateCart(key, cartVo);
                return ServerResponse.sucess();
            } else {
                // 购物车中没有这款商品，直接将商品加入购物车
                if (count>restriction){
                    return ServerResponse.error(ResponseEnum.CART_BUY_IS_NOT);
                }
                // 判断是否数据小于0
                if (count<=SysCont.ZERO){
                    cartItemVoList.removeIf(x -> x.getSkuId().longValue()==skuId);
                    updateCart(key, cartVo);
                    return ServerResponse.sucess(ResponseEnum.CART_WARNING);
                }
                CartItemVo cartItemVo = new CartItemVo();
                cartItemVo.setCount(count);
                String price = sku.getPrice().toString();
                cartItemVo.setPrice(price);
                cartItemVo.setSkuId(sku.getId());
                cartItemVo.setSkuImage(sku.getImage());
                cartItemVo.setSkuName(sku.getSkuName());
                BigDecimal subPrice = BigDecimalUtil.multiply(price, count + "");
                cartItemVo.setSubPrice(subPrice.toString());
                cartVo.getCartItemVoList().add(cartItemVo);
                //更新购物车
                updateCart(key, cartVo);
            }
        }
        return ServerResponse.sucess();
    }

    @Override
    public ServerResponse deleteCart(Long memberId, Long skuId) {
        String key = Attestation.builderCartKey(memberId);
        String cart = RedisUtil.hget(key, SysCont.CARTJSON);
        CartVo cartVo = JSON.parseObject(cart, CartVo.class);
        List<CartItemVo> cartItemVoList = cartVo.getCartItemVoList();
        cartItemVoList.removeIf(x -> x.getSkuId().longValue()==skuId.longValue());
        updateCart(key,cartVo);
        return ServerResponse.sucess();
    }

    @Override
    public ServerResponse deleteBatch(Long memberId, String ids) {
        String key = Attestation.builderCartKey(memberId);
        String cart = RedisUtil.hget(key, SysCont.CARTJSON);
        CartVo cartVo = JSON.parseObject(cart, CartVo.class);
        List<CartItemVo> cartItemVoList = cartVo.getCartItemVoList();
        Arrays.stream(ids.split(",")).forEach(x -> cartItemVoList.removeIf(y -> y.getSkuId().longValue()==Long.parseLong(x)));
        updateCart(key,cartVo);
        return ServerResponse.sucess();
    }


    private void updateCart(String key, CartVo cartVo) {
        // 更新购物车
        List<CartItemVo> cartItemVos = cartVo.getCartItemVoList();
        long totalCount = 0;
        BigDecimal totalPrice = new BigDecimal(0);
        for (CartItemVo itemVo : cartItemVos) {
            totalCount += itemVo.getCount();
            totalPrice = totalPrice.add(new BigDecimal(itemVo.getSubPrice()));
        }
        cartVo.setTotalCount(totalCount);
        cartVo.setTotalPrice(totalPrice.toString());
        RedisUtil.hset(key,SysCont.CARTCOUNT,JSON.toJSONString(cartVo.getTotalCount()));
        RedisUtil.hset(key,SysCont.CARTJSON,JSON.toJSONString(cartVo));
    }
}
