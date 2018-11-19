package cn.x5456.leyou.auth.service.impl;


import cn.x5456.leyou.auth.client.UserClient;
import cn.x5456.leyou.auth.dto.JwtProperties;
import cn.x5456.leyou.auth.entity.UserInfo;
import cn.x5456.leyou.auth.service.AuthService;
import cn.x5456.leyou.auth.utils.JwtUtils;
import cn.x5456.leyou.user.entity.TbUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties properties;

    public String authentication(String username, String password) {

        try {
            // 调用微服务，执行查询
            TbUserEntity user = this.userClient.queryUser(username, password);

            // 如果查询结果为null，则直接返回null
            if (user == null) {
                return null;
            }

            // 如果有查询结果，则生成token
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()),
                    properties.getPrivateKey(), properties.getExpire());
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}