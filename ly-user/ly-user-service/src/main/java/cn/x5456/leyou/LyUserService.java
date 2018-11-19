package cn.x5456.leyou;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 主启动类位置要注意，为了扫描@ControlledAdvice注解，实现异常处理
 */
@SpringBootApplication
@EnableDiscoveryClient
public class LyUserService {
    public static void main(String[] args) {
        SpringApplication.run(LyUserService.class, args);
    }
}