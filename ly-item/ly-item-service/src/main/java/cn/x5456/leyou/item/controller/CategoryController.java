package cn.x5456.leyou.item.controller;

import cn.x5456.leyou.item.entity.TbCategoryEntity;
import cn.x5456.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("category")
@RestController
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父节点查询商品类目
     * @param pid
     * @return
     */
    @RequestMapping("/list")
    public ResponseEntity<List<TbCategoryEntity>> queryByParentId(@RequestParam(value = "pid", defaultValue = "0") Long pid){

        List<TbCategoryEntity> list = this.categoryService.queryListByParent(pid);

        if (list != null && list.size()>0) {
            return ResponseEntity.ok(list);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

//    /**
//     * 一次性查出所有的分类（包含层级关系）
//     * @return
//     */
//    @RequestMapping("/listByLeave")
//    public ResponseEntity<List<TbCategoryEntity>> queryByLeave(){
//
////        List<TbCategoryEntity> list = this.categoryService.queryListByLeave();
////
////        if (list != null && list.size()>0) {
////            return ResponseEntity.ok(list);
////        }else {
////            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////        }
//
//        // Optional.of(null)：会报错
//        // Optional.ofNullable(null)：会new一个Optional对象
//        Optional<List<TbCategoryEntity>> optional = Optional.ofNullable(categoryService.queryListByLeave());
//
//        // 对optional中的列表进行判断，如果不为空，返回ok，为空返回404
//        return optional.map(x -> ResponseEntity.ok(x)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }

    /**
     * 根据cid3获取分类名
     * @param cid3
     * @return
     */
    @GetMapping("/{cid3}")
    public ResponseEntity<String> queryCategoryByCid3(@PathVariable(value = "cid3") Long cid3){
        return ResponseEntity.ok(categoryService.queryCategoryByCid3(cid3));
    }

}
