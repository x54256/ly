package cn.x5456.leyou.cart.service;

import cn.x5456.leyou.cart.entity.Cart;

import java.util.List;

public interface CartService {
    void addCart(Cart cart);

    List<Cart> findCart();

    void updateNum(Long skuId, Integer num);

    void deleteCart(String skuId);
}
