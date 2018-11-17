package cn.x5456.leyou.item.repository;


import cn.x5456.leyou.item.entity.TbSpecParamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecParamRepository extends JpaRepository<TbSpecParamEntity,Long> {

    List<TbSpecParamEntity> findAllByGroupId(Long gid);

    List<TbSpecParamEntity> findAllByCid(Long cid);

    List<TbSpecParamEntity> findAllByCidAndGroupId(Long cid,Long gid);

    List<TbSpecParamEntity> findAllBySearchingAndCid(Boolean searching,Long cid);
}
