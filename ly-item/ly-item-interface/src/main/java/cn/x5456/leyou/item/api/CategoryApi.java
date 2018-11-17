package cn.x5456.leyou.item.api;

import cn.x5456.leyou.item.entity.TbCategoryEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
public interface CategoryApi {


    /**
     * 根据父节点查询商品类目
     * @param pid
     * @return
     */
    @RequestMapping("/list")
    List<TbCategoryEntity> queryByParentId(@RequestParam(value = "pid", defaultValue = "0") Long pid);


    /**
     * 根据cid3获取分类名
     * @param cid3
     * @return
     */
    @GetMapping("/{cid3}")
    String queryCategoryByCid3(@PathVariable(value = "cid3") Long cid3);
}
