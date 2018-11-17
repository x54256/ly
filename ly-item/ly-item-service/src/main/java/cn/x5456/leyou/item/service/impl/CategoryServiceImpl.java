package cn.x5456.leyou.item.service.impl;

import cn.x5456.leyou.item.entity.TbCategoryEntity;
import cn.x5456.leyou.item.repository.CategoryRepository;
import cn.x5456.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * 根据pid查询分类列表
     * @param pid
     * @return
     */
    public List<TbCategoryEntity> queryListByParent(Long pid) {

        List<TbCategoryEntity> list = categoryRepository.findAllByParentId(pid);

        return list;


    }

    /**
     * 根据cid3查处商品分类==>家用电器/大 家 电/平板电视
     * @param cid3
     * @return
     */
    @Override
    public String queryCategoryByCid3(Long cid3){

        Optional<TbCategoryEntity> optional = categoryRepository.findById(cid3);

        TbCategoryEntity c = optional.get();

        Long parentId = c.getParentId();
        if (parentId != 0) {
            return queryCategoryByCid3(parentId) + "/" +c.getName();
        }
        return c.getName();

    }

}
