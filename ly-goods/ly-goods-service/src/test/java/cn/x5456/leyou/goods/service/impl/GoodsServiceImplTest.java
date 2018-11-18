package cn.x5456.leyou.goods.service.impl;

import cn.x5456.leyou.LyGoodsService;
import cn.x5456.leyou.goods.service.GoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyGoodsService.class)
public class GoodsServiceImplTest {

    @Autowired
    private GoodsService goodsService;

    @Test
    public void createHtml() {
        goodsService.createHtml(141L);
    }
}