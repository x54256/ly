package cn.x5456.leyou.item.service.impl;

import cn.x5456.leyou.item.entity.TbSpecGroupEntity;
import cn.x5456.leyou.item.entity.TbSpecParamEntity;
import cn.x5456.leyou.item.repository.SpecGroupRepository;
import cn.x5456.leyou.item.repository.SpecParamRepository;
import cn.x5456.leyou.item.service.SpecService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class SpecServiceImpl implements SpecService {

    @Resource
    private SpecGroupRepository specGroupRepository;

    @Resource
    private SpecParamRepository specParamRepository;

    /**
     * 根据规格参数组id进行查询
     * @param cid：商品分类id，一个分类下有多个规格组
     * @return
     */
    @Override
    public List<TbSpecGroupEntity> findSpecGroupById(Long cid) {
        return specGroupRepository.findAllByCid(cid);
    }

    /**
     *
     * @param gid:根据参数组id查询参数集合
     * @param cid:根据商品分类id查询参数集合（并不需要规格参数组）
     * @return
     */
    @Override
    public List<TbSpecParamEntity> findSpecParamsByGidOrCid(Long gid, Long cid) {

        List<TbSpecParamEntity> list = null;

        if (gid == null && cid != null){
            list = specParamRepository.findAllByCid(cid);
        }else if(gid != null && cid == null){
            list = specParamRepository.findAllByGroupId(gid);
        }else if(gid != null){
            list = specParamRepository.findAllByCidAndGroupId(cid,gid);
        }

        return list;
    }

    @Override
    public List<TbSpecParamEntity> findAllBySearchingAndCid(Long cid) {
        return specParamRepository.findAllBySearchingAndCid(true,cid);
    }


}
