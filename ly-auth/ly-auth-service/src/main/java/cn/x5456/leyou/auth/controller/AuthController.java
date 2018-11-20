package cn.x5456.leyou.auth.controller;


import cn.x5456.leyou.auth.dto.JwtProperties;
import cn.x5456.leyou.auth.entity.UserInfo;
import cn.x5456.leyou.auth.service.AuthService;
import cn.x5456.leyou.auth.utils.JwtUtils;
import cn.x5456.leyou.common.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@EnableConfigurationProperties(JwtProperties.class) // 只要有一个地方加载了就行，不用每个类都写
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties props;

    /**
     * 登录授权
     * http://api.leyou.com/api/auth/login
     * @param username
     * @param password
     * @return
     */
    @PostMapping("accredit")
    public ResponseEntity<Void> authentication(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response) {
        // 登录校验，返回token
        String token = this.authService.authentication(username, password);
        if (StringUtils.isBlank(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
        CookieUtils.newBuilder(response).httpOnly().maxAge(props.getCookieMaxAge()).request(request).build(props.getCookieName(), token);
        log.info("登录成功！");
        return ResponseEntity.ok().build();
    }


    /**
     * 验证用户信息（此处主要是解析jwttoken-->得到用户信息）
     * http://api.leyou.com/api/auth/verify
     * @param token
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verifyUser(@CookieValue("LY_COOKIE") String token, HttpServletRequest request, HttpServletResponse response) {
        log.info("正在经过验证");
        try {
            //从Token中获取用户信息
            UserInfo userInfo = JwtUtils.getUserInfo(props.getPublicKey(), token);
            //成功，刷新Token
            String newToken = JwtUtils.generateToken(userInfo, props.getPrivateKey(), props.getExpire());
            //将新的Token写入cookie中，并设置httpOnly
            CookieUtils.newBuilder(response).httpOnly().maxAge(props.getCookieMaxAge()).request(request).build(props.getCookieName(), newToken);
            log.info("token验证通过！");
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Token无效");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}