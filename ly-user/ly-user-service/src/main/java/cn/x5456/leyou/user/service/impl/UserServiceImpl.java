package cn.x5456.leyou.user.service.impl;

import cn.x5456.leyou.common.enums.ExceptionEnums;
import cn.x5456.leyou.common.exception.LyException;
import cn.x5456.leyou.common.utils.NumberUtils;
import cn.x5456.leyou.user.entity.TbUserEntity;
import cn.x5456.leyou.user.repository.UserRepository;
import cn.x5456.leyou.user.service.UserService;
import cn.x5456.leyou.user.utils.CodecUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private AmqpTemplate amqpTemplate;

    @Autowired  // 不能使用resource注解
    private StringRedisTemplate redisTemplate;


    private final static String CODE_PREFIX = "user:code:phone:";

    /**
     * 验证用户名和手机号是否存在
     * @param data
     * @param type
     * @return
     */
    @Override
    public TbUserEntity checkUser(String data, Integer type) {

        TbUserEntity user = null;

        if (type == 1) {
            user = userRepository.findByUsername(data);
        }
        else if (type == 2) {
            user = userRepository.findByPhone(data);
        }

        return user;
    }


    /**
     * 发送验证码
     * @param phone
     */
    @Override
    public void sendCode(String phone) {
        // 1.生成验证码
        String code = NumberUtils.generateCode(4);
        // 2.发送消息（异步）给短信微服务，发送验证码
        sendMessage(phone,code);
        // 3.将验证码保存到redis中
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(CODE_PREFIX+phone, code,60, TimeUnit.SECONDS);    // 验证码的有效时间为1min
    }

    /**
     * 新增用户接口
     * @param user
     * @return
     */
    @Transactional
    @Override
    public Boolean register(TbUserEntity user) {

        // 1.验证验证码是否正确
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String code = valueOperations.get(CODE_PREFIX + user.getPhone());
        if (StringUtils.isBlank(code)){
            log.info("输入的验证码不存在！");
            throw new LyException(ExceptionEnums.CODE_IS_EXPIRED);
        }
        if (code.equals(user.getCode())){

            //生成盐
            String salt = CodecUtils.generateSalt();
            user.setSalt(salt);

            //生成密码
            String md5Pwd = CodecUtils.md5Hex(user.getPassword(), user.getSalt());
            user.setPassword(md5Pwd);

            user.setCreated(new Date());

            try {
                userRepository.save(user);
                log.info("用户{}添加成功",user.getUsername());

                return true;
            } catch (Exception e) {
                log.info("用户{}添加失败",user.getUsername());
                e.printStackTrace();
                return false;
            }
        }

        log.info("验证码输入错误");
        throw new LyException(ExceptionEnums.CODE_IS_FAILD);

    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @Override
    public TbUserEntity queryUser(String username, String password) {
        TbUserEntity user = userRepository.findByUsername(username);
        if (user != null){
            // 获取盐
            String salt = user.getSalt();
            // 根据盐生成加密后的密码
            String md5Pwd = CodecUtils.md5Hex(password, salt);

            if (md5Pwd.equals(user.getPassword())){
                return user;
            }
        }
        return null;
    }


    /**
     * 封装发送消息的方法
     *  注意：这里要把所有异常都try起来，不能让消息的发送影响到正常的业务逻辑
     *  @param code :验证码
     *  @param phone:手机号
     */
    private void sendMessage(String phone,String code){
        // 发送消息
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("phone",phone);
            map.put("code",code);
            this.amqpTemplate.convertAndSend("sms.verify.code", map);
            log.info("消息发送成功，手机号为{}，验证码为{}",phone,code);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("消息发送异常，手机号为{}",phone);
        }
    }
}
