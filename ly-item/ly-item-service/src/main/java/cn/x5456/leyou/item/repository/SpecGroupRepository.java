package cn.x5456.leyou.item.repository;


import cn.x5456.leyou.item.entity.TbSpecGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecGroupRepository extends JpaRepository<TbSpecGroupEntity,Long> {

    List<TbSpecGroupEntity> findAllByCid(Long cid);
}
