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




