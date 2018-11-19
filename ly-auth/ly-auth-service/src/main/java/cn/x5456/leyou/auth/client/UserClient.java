package cn.x5456.leyou.auth.client;

import cn.x5456.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "user-service")
public interface UserClient extends UserApi{
}
