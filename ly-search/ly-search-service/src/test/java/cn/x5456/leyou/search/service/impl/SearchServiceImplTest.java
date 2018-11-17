package cn.x5456.leyou.search.service.impl;

import cn.x5456.leyou.LySearchService;
import cn.x5456.leyou.search.pojo.Goods;
import cn.x5456.leyou.search.repository.GoodsRepository;
import cn.x5456.leyou.search.service.SearchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchService.class)
public class SearchServiceImplTest {

    @Autowired
    private SearchService searchService;


    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @Before
    public void createIndex(){
        // 创建索引
        this.elasticsearchTemplate.createIndex(Goods.class);
        // 配置映射
        this.elasticsearchTemplate.putMapping(Goods.class);
    }

    @Test
    public void findAllGoodsList() {

        List<Goods> allGoodsList = searchService.findAllGoodsList();

//        allGoodsList.forEach(x -> System.out.println("x = " + x));
        goodsRepository.saveAll(allGoodsList);

    }



}