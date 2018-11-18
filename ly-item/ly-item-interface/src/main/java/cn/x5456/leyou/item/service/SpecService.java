package cn.x5456.leyou.item.service;

import cn.x5456.leyou.item.entity.TbSpecGroupEntity;
import cn.x5456.leyou.item.entity.TbSpecParamEntity;

import java.util.List;

public interface SpecService {
    List<TbSpecGroupEntity> findSpecGroupById(Long id);

    List<TbSpecParamEntity> findSpecParamsByGidOrCid(Long gid,Long cid);

    List<TbSpecParamEntity> findAllBySearchingAndCid(Long cid);

    List<TbSpecGroupEntity> querySpecsByCid(Long cid);

    List<TbSpecParamEntity> findAllByGenericAndCid(Long cid);
}
