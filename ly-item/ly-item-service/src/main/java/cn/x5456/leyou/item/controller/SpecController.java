package cn.x5456.leyou.item.controller;

import cn.x5456.leyou.common.enums.ExceptionEnums;
import cn.x5456.leyou.common.exception.LyException;
import cn.x5456.leyou.item.entity.TbSpecGroupEntity;
import cn.x5456.leyou.item.entity.TbSpecParamEntity;
import cn.x5456.leyou.item.service.SpecService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * specification，规格参数控制器
 * @author x5456
 */
@Slf4j
@RestController
@RequestMapping("spec")
public class SpecController {

    @Autowired
    private SpecService specService;

    /**
     * 根据规格参数组id进行查询
     * @param cid：商品分类id，一个分类下有多个规格组
     * @return
     */
    @GetMapping("/groups/{cid}")
    public ResponseEntity<List<TbSpecGroupEntity>> findSpecGroupByCid(@PathVariable Long cid){

        List<TbSpecGroupEntity> specGroupEntitys = specService.findSpecGroupById(cid);

        if (specGroupEntitys == null){
            log.error("规格参数组查询为空");
            throw new LyException(ExceptionEnums.SPEC_GROUP_CANNOT_BE_NULL);
        }

        return ResponseEntity.ok(specGroupEntitys);
    }

    /**
     * 根据规格参数组id进行查询（与上面的不同，这个需要把组下的规格参数也要查出来）
     * @param cid：商品分类id，一个分类下有多个规格组
     * @return
     */
    @GetMapping("{cid}")
    public ResponseEntity<List<TbSpecGroupEntity>> querySpecsByCid(@PathVariable("cid") Long cid){
        List<TbSpecGroupEntity> list = this.specService.querySpecsByCid(cid);
        if(list == null || list.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }


    /**
     *
     * @param gid:根据参数组id查询参数集合
     * @param cid:根据商品分类id查询参数集合（并不需要规格参数组）
     * @return
     */
    @GetMapping("/params")
    public ResponseEntity<List<TbSpecParamEntity>> findSpecParamsByGId(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid
    ){

        List<TbSpecParamEntity> specParamEntities = specService.findSpecParamsByGidOrCid(gid,cid);

        if (specParamEntities == null){
            log.error("规格查询为空");
            throw new LyException(ExceptionEnums.SPEC_PARAM_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(specParamEntities);

    }

    /**
     * 通过商品分类和是否搜索字段进行查询
     * @param cid
     * @return
     */
    @GetMapping("/params/searching/{cid}")
    public ResponseEntity<List<TbSpecParamEntity>> findAllBySearchingAndCid(@PathVariable Long cid){
        List<TbSpecParamEntity> specs = specService.findAllBySearchingAndCid(cid);
        if (specs == null) {
            throw new LyException(ExceptionEnums.SPEC_PARAM_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(specs);
    }


    /**
     * 通过商品分类和是否通用进行查询
     * @param cid
     * @return
     */
    @GetMapping("/params/generic/{cid}")
    public ResponseEntity<List<TbSpecParamEntity>> findAllByGenericAndCid(@PathVariable Long cid){
        List<TbSpecParamEntity> specs = specService.findAllByGenericAndCid(cid);
        if (specs == null) {
            throw new LyException(ExceptionEnums.SPEC_PARAM_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(specs);
    }
}
