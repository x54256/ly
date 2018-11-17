package cn.x5456.leyou.search.client;

import cn.x5456.leyou.item.api.SpecApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface SpecClient extends SpecApi {

}
