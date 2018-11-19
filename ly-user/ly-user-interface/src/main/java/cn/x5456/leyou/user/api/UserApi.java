package cn.x5456.leyou.user.api;

import cn.x5456.leyou.user.entity.TbUserEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


public interface UserApi {


    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    TbUserEntity queryUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    );

}
