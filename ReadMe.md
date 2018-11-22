# leyou项目



## 1、根据cid3查处商品分类

采用了递归，可以了解一下

```java
/**
 * 根据cid3查处商品分类==>家用电器/大 家 电/平板电视
 * @param cid3
 * @return
 */
public String queryCategoryByCid3(Long cid3){

    Optional<TbCategoryEntity> optional = categoryRepository.findById(cid3);

    TbCategoryEntity c = optional.get();

    Long parentId = c.getParentId();
    if (parentId != 0) {
        return queryCategoryByCid3(parentId) + "/" +c.getName();
    }
    return c.getName();

}
```

## 2、FastDFS的使用

FastDFS：分布式文件系统

### 2.1 安装

```shell
### 单机版安装
docker pull morunchang/fastdfs
###运行tracker
docker run -d --name tracker --net=host morunchang/fastdfs sh tracker.sh
###运行storage
docker run -d --name storage --net=host -e TRACKER_IP=10.168.31.168:22122 -e GROUP_NAME=storagegroup morunchang/fastdfs sh storage.sh
###开放端口
firewall-cmd --zone=public --add-port=22122/tcp --permanent
firewall-cmd --reload
```

| 访问图片端口 | 上传图片端口 |
| ------------ | ------------ |
| 8080         | 22122        |

### 2.2 网关配置

> 图片上传是文件的传输，如果也经过Zuul网关的代理，文件就会经过多次网路传输，造成不必要的网络负担。在高并发时，可能导致网络阻塞，Zuul网关不可用。这样我们的整个系统就瘫痪了。

所以我们在zuul中进行配置，不代理文件微服务

```yml
zuul:
  ignored-services:
      - upload-service # 忽略upload-service服务
```

修改nginx配置文件，使用它来代理api.leyou.com/api/upload对应的文件微服务：

```bash
server {
    listen       80;
    server_name  api.leyou.com;

    proxy_set_header X-Forwarded-Host $host;
    proxy_set_header X-Forwarded-Server $host;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

    # 上传路径的映射
    location /api/upload {	
        proxy_pass http://127.0.0.1:8082;
        proxy_connect_timeout 600;
        proxy_read_timeout 600;
        
        # 重写路径
        rewrite "^/api/(.*)$" /$1 break;
    }

    location / {
        proxy_pass http://127.0.0.1:10010;
        proxy_connect_timeout 600;
        proxy_read_timeout 600;
    }
}
```

- 首先，我们映射路径是/api/upload，而下面一个映射路径是 / ，根据最长路径匹配原则，/api/upload优先级更高。也就是说，凡是以/api/upload开头的路径，都会被第一个配置处理

- `proxy_pass`：反向代理，这次我们代理到8082端口，也就是upload-service服务

- `rewrite "^/api/(.*)$" /$1 break`，路径重写：

  - `"^/api/(.*)$"`：匹配路径的正则表达式，用了分组语法，把`/api/`以后的所有部分当做1组

  - `/$1`：重写的目标路径，这里用$1引用前面正则表达式匹配到的分组（组编号从1开始），即`/api/`后面的所有。这样新的路径就是除去`/api/`以外的所有，就达到了去除`/api`前缀的目的

  - `break`：指令，常用的有2个，分别是：last、break

    - last：重写路径结束后，将得到的路径重新进行一次路径匹配
    - break：重写路径结束后，不再重新匹配路径。

    我们这里不能选择last，否则以新的路径/upload/image来匹配，就不会被正确的匹配到8082端口了

修改完成，输入`nginx -s reload`命令重新加载配置。然后再次上传。

> 配置文件和代码，见ly-upload微服务，访问图片是，记得修改hosts文件


## 3、mvc使用异常

> Spring MVC Content type 'application/x-www-form-urlencoded' not supported

原因，错误使用@RequestBody注解，前端传来的并不是json，而去当作json解析了

`jackson的配置`：

    spring:
      jackson:
          default-property-inclusion: non_null  # jackson返回的数据为null，不返回


## 4、JPA使用问题

    1。不要把不是表的实体加上@Entity注解，否则会有意想不到的后果
        1）多了个dtype字段
    2。自己封装接收参数的实体，一定要看下前端是怎么穿的
        1）到底是采用继承的方式，还是直接在类中写就行了
    3。@Transient注解，表示数据库中没有。
        1）建议不要使用，重新写一个实体类（dto）更加正式
    4。最好不要使用dozer
        1）会带来性能上的消耗
        2）配置太多（好吧，这个是主要原因）    
        3）dozer的使用：https://blog.csdn.net/banjuer/article/details/80411943
            3.1）缺了个@Configuration注解
            3.2）缺了个转换器：EnumIntegerBiDirectionalDozerConverter
                https://blog.csdn.net/banjuer/article/details/80417441
    5。jpa更新和删除注意事项
        @Modifying
        @Query("update ShopCoupon sc set sc.deleted = true where sc.id in :ids")
        public void deleteByIds(@Param(value = "ids") List<String> ids);
        
        注：
        1）update或delete时必须使用@Modifying对方法进行注解，才能使得ORM知道现在要执行的是写操作
        2）有时候不加@Param注解参数，可能会报如下异常：
            org.springframework.dao.InvalidDataAccessApiUsageException: Name must not be null or empty!; nested exception is java.lang.IllegalArgumentException: Name must not be null or empty!
        3）当使用集合作为条件时，可参考此处的ids   


## 5、fegin使用问题

> java.lang.IllegalStateException: PathVariable annotation was empty on param 0.

    使用Feign的时候,如果参数中带有@PathVariable形式的参数,则要用value=""标明对应的参数,否则会抛出IllegalStateException异常
        同类异常还有@RequestParam注解

所以在写接口时，一定要规范，要不就写好，要不就不写


## 6、使用Java8新特性，简化数据库交互

```java
    @Override
    public List<TbSpecGroupEntity> querySpecsByCid(Long cid) {

        List<TbSpecGroupEntity> specGroupById = this.findSpecGroupById(cid);

        // TODO: 2018/11/18 这中方式，要与数据库进行多次交互，数据量大时时间很长
//        specGroupById.forEach(x -> {
//            // 根据组id查询所有的参数
//            specParamRepository.findAllByGroupId(x.getId());
//        });

        // 一次性查出所有的params
        List<TbSpecParamEntity> allByCid = specParamRepository.findAllByCid(cid);
        // 将其进行根据组id进行分组，key是组id，value是组下的参数列表
        Map<Long, List<TbSpecParamEntity>> map = allByCid.stream().collect(Collectors.groupingBy(TbSpecParamEntity::getGroupId));

        specGroupById.forEach(x -> x.setParams(map.get(x.getId())));


        return specGroupById;
    }

```

## 7、SpringAMQP消费者端重试机制

    spring.rabbitmq.listener.simple.retry.max-attempts=5  最大重试次数
    spring.rabbitmq.listener.simple.retry.enabled=true 是否开启消费者重试（为false时关闭消费者重试，这时消费端代码异常会一直重复收到消息）
    spring.rabbitmq.listener.simple.retry.initial-interval=5000 重试间隔时间（单位毫秒）
    spring.rabbitmq.listener.simple.default-requeue-rejected=false 重试次数超过上面的设置之后是否丢弃（false不丢弃时需要写相应代码将该消息加入死信队列）

## 8、@Autowired与@Resource的区别

@Autowired按byType自动注入，而@Resource默认按 byName自动注入，所以此处使用Resource会有错误

![image-20181119163712028](https://ws1.sinaimg.cn/large/006tNbRwly1fxdgki0u9ej30qo03w3zd.jpg)

## 9、HIbrenate数据校验

我们在`ly-user-interface`中添加Hibernate-Validator依赖：

```xml
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
</dependency>
```

我们在User对象的部分属性上添加注解：

```java
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Length(min = 4, max = 30, message = "用户名只能在4~30位之间")
    private String username;// 用户名

    @JsonIgnore
    @Length(min = 4, max = 30, message = "用户名只能在4~30位之间")
    private String password;// 密码

    @Pattern(regexp = "^1[35678]\\d{9}$", message = "手机号格式不正确")
    private String phone;// 电话

    private Date created;// 创建时间

    @JsonIgnore
    private String salt;// 密码的盐值
}
```

在controller上进行控制

在controller中只需要给User添加 @Valid注解即可。

![image-20181119164102952](https://ws2.sinaimg.cn/large/006tNbRwly1fxdgoibcc7j30ys0eutc0.jpg)

### Bean校验的注解：

| **Constraint**                                     | **详细信息**                                                 |
| -------------------------------------------------- | ------------------------------------------------------------ |
| **@Valid**                                         | 被注释的元素是一个对象，需要检查此对象的所有字段值           |
| **@Null**                                          | 被注释的元素必须为 null                                      |
| **@NotNull**                                       | 被注释的元素必须不为 null                                    |
| **@AssertTrue**                                    | 被注释的元素必须为 true                                      |
| **@AssertFalse**                                   | 被注释的元素必须为 false                                     |
| **@Min(value)**                                    | 被注释的元素必须是一个数字，其值必须大于等于指定的最小值     |
| **@Max(value)**                                    | 被注释的元素必须是一个数字，其值必须小于等于指定的最大值     |
| **@DecimalMin(value)**                             | 被注释的元素必须是一个数字，其值必须大于等于指定的最小值     |
| **@DecimalMax(value)**                             | 被注释的元素必须是一个数字，其值必须小于等于指定的最大值     |
| **@Size(max,   min)**                              | 被注释的元素的大小必须在指定的范围内                         |
| **@Digits   (integer, fraction)**                  | 被注释的元素必须是一个数字，其值必须在可接受的范围内         |
| **@Past**                                          | 被注释的元素必须是一个过去的日期                             |
| **@Future**                                        | 被注释的元素必须是一个将来的日期                             |
| **@Pattern(value)**                                | 被注释的元素必须符合指定的正则表达式                         |
| **@Email**                                         | 被注释的元素必须是电子邮箱地址                               |
| **@Length**                                        | 被注释的字符串的大小必须在指定的范围内                       |
| **@NotEmpty**                                      | 被注释的字符串的必须非空                                     |
| **@Range**                                         | 被注释的元素必须在合适的范围内                               |
| **@NotBlank**                                      | 被注释的字符串的必须非空                                     |
| **@URL(protocol=,host=,   port=,regexp=, flags=)** | 被注释的字符串必须是一个有效的url                            |
| **@CreditCardNumber**                              | 被注释的字符串必须通过Luhn校验算法，银行卡，信用卡等号码一般都用Luhn计算合法性 |

## 10、nignx，zuul转发的坑

![image-20181119163838117](https://ws1.sinaimg.cn/large/006tNbRwly1fxdglyzsq7j30sa0diwgs.jpg)



![image-20181119163937162](https://ws2.sinaimg.cn/large/006tNbRwly1fxdgn0xx10j317w0h2jy9.jpg)

敏感头过滤的解决方案有以下两种：
​         
全局设置：

- `zuul.sensitive-headers=` 

指定路由设置：

- `zuul.routes.<routeName>.sensitive-headers=`
- `zuul.routes.<routeName>.custom-sensitive-headers=true`

## 11、获取cookie的注解

    @CookieValue("${ly.jwt.cookieName}")
        ${ly.jwt.cookieName}:这种方式会有坑，还是写死把

## 12、JWT架构图

![image-20181119164614555](https://ws2.sinaimg.cn/large/006tNbRwly1fxdgtw6ckgj316e0s0dl0.jpg)

## 13、购物车架构图

![image-20181119165517217](https://ws4.sinaimg.cn/large/006tNbRwly1fxdh3c104ej30vn0u07b9.jpg)


spring:
  cloud:
    config:
      name: item
      profile: dev
      label: master # 主分支
      uri: http://127.0.0.1:12000 # 配置中心微服务地址