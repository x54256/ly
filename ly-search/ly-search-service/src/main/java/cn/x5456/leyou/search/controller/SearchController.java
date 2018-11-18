package cn.x5456.leyou.search.controller;

import cn.x5456.leyou.common.enums.ExceptionEnums;
import cn.x5456.leyou.common.exception.LyException;
import cn.x5456.leyou.common.pojo.PageResult;
import cn.x5456.leyou.search.pojo.Goods;
import cn.x5456.leyou.search.pojo.SearchRequest;
import cn.x5456.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// TODO: 2018/11/18 此处只做了基本的查询，聚合工作没有做
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping
    public ResponseEntity<PageResult<Goods>> searchGoods(@RequestBody SearchRequest searchRequest){
        PageResult<Goods> goodsPageResult = searchService.searchGoods(searchRequest);
        if (goodsPageResult == null){
            throw new LyException(ExceptionEnums.SEARCH_CANNOT_BE_FOUND);
        }
        return ResponseEntity.ok(goodsPageResult);
    }

}
