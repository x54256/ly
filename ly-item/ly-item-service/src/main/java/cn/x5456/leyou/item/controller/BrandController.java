package cn.x5456.leyou.item.controller;

import cn.x5456.leyou.common.enums.ExceptionEnums;
import cn.x5456.leyou.common.exception.LyException;
import cn.x5456.leyou.common.pojo.PageResult;
import cn.x5456.leyou.item.entity.TbBrandEntity;
import cn.x5456.leyou.item.service.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("brand")
@RestController
public class BrandController {


    @Autowired
    private BrandService brandService;

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
    public ResponseEntity<PageResult<TbBrandEntity>> queryBrandOfPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page, // jpa的分页查询是从0开始的，，我也很绝望。。
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc",required = false) Boolean desc,
            @RequestParam(value = "key", defaultValue = "") String key){


        PageResult<TbBrandEntity> pageResult = brandService.queryBrandByPageAndSort(page - 1, rows, sortBy, desc, key);

        // 判断返回的商品列表是否为空
        if (pageResult == null){
            throw new LyException(ExceptionEnums.Brand_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(pageResult);

    }

    /**
     * 新增品牌，什么都不带 ==> /
     * @param brand
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addBrand(TbBrandEntity brand){

        try {
            brandService.addBrand(brand);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new LyException(ExceptionEnums.Brand_INSERT_FAILD);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /**
     * 根据三级分类查询所有品牌
     * @param cid
     * @return
     */
    @GetMapping("/cid/{cid}")
    public ResponseEntity<List<TbBrandEntity>> findBrandsByCid(@PathVariable Long cid){

        List<TbBrandEntity> brands = brandService.findBrandsByCid3(cid);
        if (brands == null) {
            log.error("根据cid3查询商品品牌为空");
            throw new LyException(ExceptionEnums.Brand_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(brands);
    }

    /**
     * 根据品牌id查询品牌
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<TbBrandEntity> findBrandById(@PathVariable Long id){
        TbBrandEntity brand = brandService.findBrandById(id);
        if (brand == null){
            throw new LyException(ExceptionEnums.Brand_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(brand);
    }

}
