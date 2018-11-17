package cn.x5456.leyou.item.service;

import cn.x5456.leyou.common.pojo.PageResult;
import cn.x5456.leyou.item.entity.TbBrandEntity;

import java.util.List;

public interface BrandService {
    PageResult<TbBrandEntity> queryBrandByPageAndSort(Integer i, Integer rows, String sortBy, Boolean desc, String key);

    void addBrand(TbBrandEntity brand);

    String queryBrandById(Long brandId);

    List<TbBrandEntity> findBrandsByCid3(Long cid);

    TbBrandEntity findBrandById(Long id);
}
