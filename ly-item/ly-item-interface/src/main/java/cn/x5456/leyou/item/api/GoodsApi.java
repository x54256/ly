package cn.x5456.leyou.item.api;


import cn.x5456.leyou.common.pojo.PageResult;
import cn.x5456.leyou.item.dto.GoodsDTO;
import cn.x5456.leyou.item.entity.TbSkuEntity;
import cn.x5456.leyou.item.entity.TbSpuDetailEntity;
import cn.x5456.leyou.item.entity.TbSpuEntity;
import cn.x5456.leyou.order.dto.CartDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;


public interface GoodsApi {

    /**
     * 分页查询SPU
     * @param page
     * @param rows
     * @param key
     * @return
     */
    @GetMapping("/spu/page")
    PageResult<TbSpuEntity> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", defaultValue = "true" ,required = false) Boolean saleable,
            @RequestParam(value = "key",defaultValue = "",required = false) String key);

    /**
     * 新增商品
     * @param goodsDTO:商品DTO
     * @return
     */
    @PostMapping("goods")
    Void saveGoods(@RequestBody GoodsDTO goodsDTO);

    /**
     * 更新商品信息
     * @param goodsDTO:商品DTO
     * @return
     */
    @PutMapping("goods")
    Void updateGoods(@RequestBody GoodsDTO goodsDTO);


    /**
     * 根据id查询商品通用信息
     * @param id
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    TbSpuEntity querySpuById(@PathVariable(value = "id") Long id);


    /**
     * 根据spuid查询商品具体信息 ==> 所有sku
     * @param id:spuId
     * @return
     */
    @GetMapping("/sku/list")
    List<TbSkuEntity> querySkuById(@RequestParam(value = "id") Long id);


    /**
     * 查询所有spu
     * @return
     */
    @GetMapping("/spu/list")
    List<TbSpuEntity> findAllSpu();

    /**
     * 根据spuId查询商品详细信息
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail")
    TbSpuDetailEntity findSpuDetailBySpuId(@RequestParam(value = "spuId") Long spuId);


    /**
     * 根据skuId查询Sku
     * @param ids:skuIds
     * @return
     */
    @GetMapping("/sku/list/ids")
    List<TbSkuEntity> querySkuBySkuId(@RequestParam(value = "ids") Long[] ids);

    /**
     * 减少商品库存
     * @param skuId:商品id
     * @param num:购买数量
     * @return
     */
    @PutMapping("/stock/decrease")
    Void decreaseStock(@RequestBody List<CartDTO> cartDTOS);
}