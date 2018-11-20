import cn.x5456.leyou.LySmsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySmsService.class)
public class a {

    @Autowired
    private StringRedisTemplate redisTemplate;
    // 如果key和value都是String可以使用StringRedisTemplate

    @Test
    public void hello(){
//        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        /*
        redisTemplate.opsForValue();//操作字符串
        redisTemplate.opsForHash();//操作hash
        	redisTemplate.boundHashOps();// 提前制定hash的key，省的每次都要写
        redisTemplate.opsForList();//操作list
        redisTemplate.opsForSet();//操作set
        redisTemplate.opsForZSet();//操作有序set
         */
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        valueOperations.set("hello", "redis");
//        System.out.println("useRedisDao = " + valueOperations.get("hello"));

    }
}