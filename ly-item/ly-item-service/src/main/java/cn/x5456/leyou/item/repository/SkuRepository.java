package cn.x5456.leyou.item.repository;


import cn.x5456.leyou.item.entity.TbSkuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkuRepository extends JpaRepository<TbSkuEntity,Long> {

    List<TbSkuEntity> findAllBySpuId(Long spuId);

    void deleteBySpuId(Long spuId);
}
