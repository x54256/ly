package cn.x5456.leyou.user.controller;

import cn.x5456.leyou.common.enums.ExceptionEnums;
import cn.x5456.leyou.common.exception.LyException;
import cn.x5456.leyou.user.entity.TbUserEntity;
import cn.x5456.leyou.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 验证用户名和手机号是否存在
     * @param data:要校验的数据
     * @param type:要校验的数据类型：1，用户名；2，手机；
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable(value = "data") String data,@PathVariable(value = "type")Integer type){

        TbUserEntity user = userService.checkUser(data, type);

        if (user == null){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);

    }

    /**
     * 发送验证码
     * @return
     */
    @PostMapping("/code")
    public ResponseEntity<Void> sendCode(@RequestParam("phone") String phone){

        userService.sendCode(phone);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * 用户注册
     * @param userEntity
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid TbUserEntity userEntity,BindingResult result){   // 加上@Valid注解，实现数据格式的校验

        if(result.hasErrors()){
            List<ObjectError> ls = result.getAllErrors();
            for (int i = 0; i < ls.size(); i++) {
                System.out.println("error:"+ls.get(i));
            }
        }

        Boolean flag = userService.register(userEntity);

        if (!flag){
            throw new LyException(ExceptionEnums.USER_REGISTER_ERROR);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public ResponseEntity<TbUserEntity> queryUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        TbUserEntity user = this.userService.queryUser(username, password);
        if (user == null) {
            throw new LyException(ExceptionEnums.USER_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(user);
    }

}
