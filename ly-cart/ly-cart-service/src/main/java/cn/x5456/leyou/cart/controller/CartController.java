package cn.x5456.leyou.cart.controller;

import cn.x5456.leyou.cart.entity.Cart;
import cn.x5456.leyou.cart.service.CartService;
import cn.x5456.leyou.common.enums.ExceptionEnums;
import cn.x5456.leyou.common.exception.LyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 商品添加购物车
     * @param cart
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart) {

        cartService.addCart(cart);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * 查询购物车列表
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCartList() {
        List<Cart> cartList = cartService.findCart();
        if (cartList == null){
            log.error("购物车查询为空!");
            throw new LyException(ExceptionEnums.CART_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(cartList);
    }


    /**
     * 更新购物车中商品数量
     * @param skuId
     * @param num
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestParam("id") Long skuId,
                                          @RequestParam("num") Integer num) {
        this.cartService.updateNum(skuId, num);
        return ResponseEntity.ok().build();
    }


    /**
     * 删除购物车中数据
     * @param skuId
     * @return
     */
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") String skuId) {
        this.cartService.deleteCart(skuId);
        return ResponseEntity.ok().build();
    }


}
