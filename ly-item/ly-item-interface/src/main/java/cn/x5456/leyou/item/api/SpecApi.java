package cn.x5456.leyou.item.api;

import cn.x5456.leyou.item.entity.TbSpecGroupEntity;
import cn.x5456.leyou.item.entity.TbSpecParamEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * specification，规格参数控制器
 * @author x5456
 */
@RequestMapping("spec")
public interface SpecApi {

    /**
     * 根据规格参数组id进行查询
     * @param cid：商品分类id，一个分类下有多个规格组
     * @return
     */
    @GetMapping("/groups/{cid}")
    List<TbSpecGroupEntity> findSpecGroupByCid(@PathVariable(value = "cid") Long cid);


    /**
     *
     * @param gid:根据参数组id查询参数集合
     * @param cid:根据商品分类id查询参数集合（并不需要规格参数组）
     * @return
     */
    @GetMapping("/params")
    List<TbSpecParamEntity> findSpecParamsByGId(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid
    );

    /**
     * 通过商品分类和是否搜索字段进行查询
     * @param cid
     * @return
     */
    @GetMapping("/params/searching/{cid}")
    List<TbSpecParamEntity> findAllBySearchingAndCid(@PathVariable(value = "cid") Long cid);
}
