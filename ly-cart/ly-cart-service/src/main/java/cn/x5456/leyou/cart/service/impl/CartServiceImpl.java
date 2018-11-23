package cn.x5456.leyou.cart.service.impl;

import cn.x5456.leyou.auth.entity.UserInfo;
import cn.x5456.leyou.cart.client.GoodsClient;
import cn.x5456.leyou.cart.entity.Cart;
import cn.x5456.leyou.cart.interceptor.LoginInterceptor;
import cn.x5456.leyou.cart.service.CartService;
import cn.x5456.leyou.common.utils.JsonUtils;
import cn.x5456.leyou.item.entity.TbSkuEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private GoodsClient goodsClient;


    private static final String CART_PREFIX = "cart:userid:";

    /**
     * 添加商品到购物车中
     * @param cart
     */
    @Override
    public void addCart(Cart cart) {
        // redis中的信息应该是这样的：{用户id1:{商品id1:商品信息1,...},...}

        // 1.获取用户对象
        UserInfo user = LoginInterceptor.getLoginUser();
        // 2.获取操作hash的对象，指定key为用户id
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(CART_PREFIX + user.getId());
        // 3.获取传来的购物车信息，商品id
        Long skuId = cart.getSkuId();
        // 4.判断商品是否存在redis中
        if (hashOps.hasKey(skuId.toString())){
            Integer num = cart.getNum();
            cart = JsonUtils.toBean(hashOps.get(skuId.toString()).toString(), Cart.class);
            cart.setNum(num + cart.getNum());
        }else {
            // 不存在，新增购物车数据
            cart.setUserId(user.getId());
            // 其它商品信息， 需要查询商品服务
            TbSkuEntity sku = this.goodsClient.querySkuBySkuId(new Long[]{skuId}).get(0);
            // 对images进行切割，如果没有,的话，返回整个字符串
            cart.setImage(StringUtils.substringBefore(sku.getImages(),","));
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
        }
        // 5.放回redis中
        hashOps.put(skuId.toString(),JsonUtils.toString(cart));

    }

    /**
     * 查询购物车
     * @return
     */
    @Override
    public List<Cart> findCart() {

        // 1.获取用户信息
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        String key = CART_PREFIX + loginUser.getId();
        // 判断是否存在
        if (!redisTemplate.hasKey(key)){
            return null;
        }
        // 2.从redis中取出数据
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        /*
            entities：获取字典形数据
            keys：获取所有商品id的数据
            values:获取所有的商品数据
         */
        List<Object> values = hashOps.values();
        // 3.对集合中的每个元素转成cart类型
        List<Cart> cartList = values.stream().map(x -> JsonUtils.toBean(x.toString(), Cart.class)).collect(Collectors.toList());

        return cartList;
    }

    /**
     * 更新购物车商品数量
     * @param skuId
     * @param num
     */
    @Override
    public void updateNum(Long skuId, Integer num) {

        // 1.获取用户信息
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        // 2.从redis中取出数据
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(CART_PREFIX + loginUser.getId());
        Object json = hashOps.get(skuId.toString());
        // 3.修改购物车中商品数量
        Cart cart = JsonUtils.toBean(json.toString(), Cart.class);
        cart.setNum(cart.getNum() + num);
        // 4.保存
        hashOps.put(skuId.toString(),JsonUtils.toString(cart));
    }

    /**
     * 删除购物车中数据
     * @param skuId
     * @return
     */
    @Override
    public void deleteCart(String skuId) {
        // 1.获取用户信息
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        // 2.从redis中删除数据
        redisTemplate.opsForHash().delete(CART_PREFIX + loginUser.getId(),skuId);
//        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(CART_PREFIX + loginUser.getId());
//        hashOps.delete(skuId);
    }
}
