package cn.x5456.leyou.item.repository;


import cn.x5456.leyou.item.entity.TbCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<TbCategoryEntity,Long> {

    List<TbCategoryEntity> findAllByParentId(Long pid);


//    List<TbCategoryEntity> findAllByCategoryEntities_Empty();


}
