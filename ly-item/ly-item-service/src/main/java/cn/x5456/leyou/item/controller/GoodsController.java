package cn.x5456.leyou.item.controller;


import cn.x5456.leyou.common.enums.ExceptionEnums;
import cn.x5456.leyou.common.exception.LyException;
import cn.x5456.leyou.common.pojo.PageResult;
import cn.x5456.leyou.item.dto.GoodsDTO;
import cn.x5456.leyou.item.entity.TbSkuEntity;
import cn.x5456.leyou.item.entity.TbSpuDetailEntity;
import cn.x5456.leyou.item.entity.TbSpuEntity;
import cn.x5456.leyou.item.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 分页查询SPU
     * @param page
     * @param rows
     * @param key
     * @return
     */
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<TbSpuEntity>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", defaultValue = "true" ,required = false) Boolean saleable,
            @RequestParam(value = "key",defaultValue = "",required = false) String key) {
        // 分页查询spu信息
        PageResult<TbSpuEntity> result = this.goodsService.querySpuByPageAndSort(page, rows,saleable, key);

        return ResponseEntity.ok(result);
    }

    /**
     * 新增商品
     * @param goodsDTO:商品DTO
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody GoodsDTO goodsDTO){

        try {
            goodsService.saveGoods(goodsDTO);
        }catch (Exception ex){
            log.error(ex.getMessage());
            ex.printStackTrace();
            throw new LyException(ExceptionEnums.GOODS_INSERT_FAILD);
        }

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * 新增商品
     * @param goodsDTO:商品DTO
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody GoodsDTO goodsDTO){

        try {
            goodsService.updateGoods(goodsDTO);
        }catch (Exception ex){
            log.error(ex.getMessage());
            ex.printStackTrace();
            throw new LyException(ExceptionEnums.GOODS_UPDATE_FAILD);
        }

        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    /**
     * 根据id查询商品通用信息
     * @param id
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<TbSpuEntity> querySpuById(@PathVariable(value = "id") Long id){

        TbSpuEntity spuEntity = goodsService.querySpuById(id);
        if (spuEntity == null){
            log.error("spu查询为空");
            throw new LyException(ExceptionEnums.SPU_CANNOT_BE_NULL);
        }

        return ResponseEntity.ok(spuEntity);
    }


    /**
     * 根据spuid查询商品具体信息 ==> 所有sku
     * @param id:spuId
     * @return
     */
    @GetMapping("/sku/list")
    public ResponseEntity<List<TbSkuEntity>> querySkuById(@RequestParam(value = "id") Long id){

        List<TbSkuEntity> spuEntity = goodsService.querySkuById(id);
        if (spuEntity == null){
            log.error("sku查询为空");
            throw new LyException(ExceptionEnums.SKU_CANNOT_BE_NULL);
        }

        return ResponseEntity.ok(spuEntity);
    }

    /**
     * 根据skuId查询Sku
     * //http://api.leyou.com/api/item/sku/list/ids?ids=27359021703
     * @param ids:skuId
     * @return
     */
    @GetMapping("/sku/list/ids")
    public ResponseEntity<List<TbSkuEntity>> querySkuBySkuId(@RequestParam(value = "ids") Long[] ids){

        List<TbSkuEntity> skus = goodsService.querySkuBySkuId(ids);

        if (skus == null){
            log.error("sku查询为空");
            throw new LyException(ExceptionEnums.SKU_CANNOT_BE_NULL);
        }

        return ResponseEntity.ok(skus);
    }

    /**
     * 查询所有spu
     * @return
     */
    @GetMapping("/spu/list")
    public ResponseEntity<List<TbSpuEntity>> findAllSpu(){
        List<TbSpuEntity> spuEntities = goodsService.findAllSpu();
        if (spuEntities == null){
            throw new LyException(ExceptionEnums.SPU_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(spuEntities);
    }

    /**
     * 根据spuId查询商品详细信息
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail")
    public ResponseEntity<TbSpuDetailEntity> findSpuDetailBySpuId(@RequestParam(value = "spuId") Long spuId){
        TbSpuDetailEntity detailEntity = goodsService.querySpuDetailBySpuId(spuId);
        if (detailEntity == null){
            throw new LyException(ExceptionEnums.SPU_DETAIL_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(detailEntity);
    }

}