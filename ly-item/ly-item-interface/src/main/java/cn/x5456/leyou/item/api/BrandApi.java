package cn.x5456.leyou.item.api;

import cn.x5456.leyou.common.pojo.PageResult;
import cn.x5456.leyou.item.entity.TbBrandEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("brand")
public interface BrandApi {

    /**
     * 解决方案：在做查询的时候，考虑一下是否需要一个通用的查询接口（包括模糊，分页等功能）
     *
     * page：当前页，int
     * rows：每页大小，int
     * sortBy：排序字段，String
     * desc：是否为降序，boolean
     * key：搜索关键词，String
     * @return
     */
    @GetMapping("/page")
    PageResult<TbBrandEntity> queryBrandOfPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page, // jpa的分页查询是从0开始的，，我也很绝望。。
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc",required = false) Boolean desc,
            @RequestParam(value = "key", defaultValue = "") String key);



    /**
     * 新增品牌，什么都不带 ==> /
     * @param brand
     * @return
     */
    @PostMapping
    Void addBrand(TbBrandEntity brand);


    /**
     * 根据三级分类查询所有品牌
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    List<TbBrandEntity> findBrandsByCid(@PathVariable(value = "cid") Long cid);


    /**
     * 根据品牌id查询品牌
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    TbBrandEntity findBrandById(@PathVariable(value = "id") Long id);


}
