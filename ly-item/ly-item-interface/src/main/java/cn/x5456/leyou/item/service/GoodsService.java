package cn.x5456.leyou.item.service;

import cn.x5456.leyou.common.pojo.PageResult;
import cn.x5456.leyou.item.dto.GoodsDTO;
import cn.x5456.leyou.item.entity.TbSkuEntity;
import cn.x5456.leyou.item.entity.TbSpuDetailEntity;
import cn.x5456.leyou.item.entity.TbSpuEntity;

import java.util.List;

public interface GoodsService {
    PageResult<TbSpuEntity> querySpuByPageAndSort(Integer page, Integer rows, Boolean saleable, String key);

    void saveGoods(GoodsDTO goodsDTO);

    void updateGoods(GoodsDTO goodsDTO);

    TbSpuEntity querySpuById(Long id);

    List<TbSkuEntity> querySkuById(Long spuId);

    List<TbSpuEntity> findAllSpu();

    TbSpuDetailEntity querySpuDetailBySpuId(Long spuId);
}
