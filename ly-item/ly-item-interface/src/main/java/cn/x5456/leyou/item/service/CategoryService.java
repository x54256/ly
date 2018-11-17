package cn.x5456.leyou.item.service;

import cn.x5456.leyou.item.entity.TbCategoryEntity;

import java.util.List;

public interface CategoryService {
    List<TbCategoryEntity> queryListByParent(Long pid);

    // 获取分类名 ==> 家用电器/大 家 电/平板电视
    String queryCategoryByCid3(Long cid3);
}
