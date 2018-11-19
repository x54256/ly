package cn.x5456.leyou.user.service;

import cn.x5456.leyou.user.entity.TbUserEntity;

public interface UserService {
    TbUserEntity checkUser(String data, Integer type);

    void sendCode(String phone);

    Boolean register(TbUserEntity userEntity);

    TbUserEntity queryUser(String username, String password);
}
