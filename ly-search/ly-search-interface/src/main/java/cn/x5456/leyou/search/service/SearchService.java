package cn.x5456.leyou.search.service;

import cn.x5456.leyou.common.pojo.PageResult;
import cn.x5456.leyou.search.pojo.Goods;
import cn.x5456.leyou.search.pojo.SearchRequest;

import java.util.List;

public interface SearchService {
    List<Goods> findAllGoodsList();

    PageResult<Goods> searchGoods(SearchRequest searchRequest);
}
