package cn.x5456.leyou.order.client;


import cn.x5456.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsApi {
}
