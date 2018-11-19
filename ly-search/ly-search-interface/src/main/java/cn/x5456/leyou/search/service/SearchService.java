package cn.x5456.leyou.search.service;

import cn.x5456.leyou.common.pojo.PageResult;
import cn.x5456.leyou.search.pojo.Goods;
import cn.x5456.leyou.search.pojo.SearchRequest;

import java.io.IOException;
import java.util.List;

public interface SearchService {
    List<Goods> findAllGoodsList();

    PageResult<Goods> searchGoods(SearchRequest searchRequest);

    void createIndex(Long id) throws IOException;

    void deleteIndex(Long id);
}
