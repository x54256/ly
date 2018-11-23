package cn.x5456.leyou.cart.listener;

import cn.x5456.leyou.common.enums.ExceptionEnums;
import cn.x5456.leyou.common.exception.LyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 删除购物车监听器
 */
@Slf4j
@Component
public class StockListener {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String CART_PREFIX = "cart:userid:";

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "leyou.stock.cart.queue"),
            exchange = @Exchange(name = "leyou.stock.exchange", type = ExchangeTypes.TOPIC),
            key = "cart.sku.stock"
    ))
    public void listenCartStock(Map<String, Object> msg) {
        if (msg == null){
            log.error("接收到的消息为空！");
            throw new LyException(ExceptionEnums.MESSAGE_IS_NULL);
        }
        // 1.获取用户id
        String userId = msg.get("userId").toString();
        // 2.根据id获取用户购物车
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(CART_PREFIX + userId);
        // 3.删除相应的商品
        Set<Long> skuIds = (HashSet<Long>) msg.get("skuIds");
        skuIds.forEach(x -> {
            if (hashOps.hasKey(x.toString())){  // 要转成string类型
                hashOps.delete(x.toString());
            }
        });
        log.info("购物车商品删除成功！");

    }


}