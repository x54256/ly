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
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SpecServiceImpl implements SpecService {

    @Resource
    private SpecGroupRepository specGroupRepository;

    @Resource
    private SpecParamRepository specParamRepository;

    /**
     * 根据分类id进行查询下面的多个规格组
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

    /**
     * 根据分类id获取规格参数组的所有信息（包括参数）
     * @param cid
     * @return
     */
    @Override
    public List<TbSpecGroupEntity> querySpecsByCid(Long cid) {

        List<TbSpecGroupEntity> specGroupById = this.findSpecGroupById(cid);

        // TODO: 2018/11/18 这中方式，要与数据库进行多次交互，数据量大时时间很长
//        specGroupById.forEach(x -> {
//            // 根据组id查询所有的参数
//            x.setParams(specParamRepository.findAllByGroupId(x.getId()));
//        });

        // 一次性查出所有的params
        List<TbSpecParamEntity> allByCid = specParamRepository.findAllByCid(cid);
        // 将其进行根据组id进行分组，key是组id，value是组下的参数列表
        Map<Long, List<TbSpecParamEntity>> map = allByCid.stream().collect(Collectors.groupingBy(TbSpecParamEntity::getGroupId));

        specGroupById.forEach(x -> x.setParams(map.get(x.getId())));


        return specGroupById;
    }

    /**
     * 通过商品分类和是否通用进行查询
     * @param cid
     * @return
     */
    @Override
    public List<TbSpecParamEntity> findAllByGenericAndCid(Long cid) {

        return specParamRepository.findAllByGenericAndCid(false,cid);
    }


}
