package cn.x5456.leyou.auth.dto;


import cn.x5456.leyou.auth.utils.RsaUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {

    private String secret; // 密钥

    private String pubKeyPath;// 公钥路径

    private String priKeyPath;// 私钥路径

    private int expire;// token过期时间

    private PublicKey publicKey; // 公钥

    private PrivateKey privateKey; // 私钥

    private String CookieName;  // cookie名

    private int CookieMaxAge;   // cookie过期时间

    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    /**
     * @PostContruct：在构造方法执行之后执行该方法
     * 相当于 <bean id="" init="xxx"/>
     */
    @PostConstruct
    public void init(){
        try {
            File pubKey = new File(pubKeyPath);
            File priKey = new File(priKeyPath);
            if (!pubKey.exists() || !priKey.exists()) {
                // 生成公钥和私钥
                RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
            }
            // 获取公钥和私钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        } catch (Exception e) {
            logger.error("初始化公钥和私钥失败！", e);
            throw new RuntimeException(e);
        }
    }
}