package cn.x5456.leyou.goods.service.impl;

import cn.x5456.leyou.common.enums.ExceptionEnums;
import cn.x5456.leyou.common.exception.LyException;
import cn.x5456.leyou.goods.client.BrandClient;
import cn.x5456.leyou.goods.client.CategoryClient;
import cn.x5456.leyou.goods.client.GoodsClient;
import cn.x5456.leyou.goods.client.SpecClient;
import cn.x5456.leyou.goods.service.GoodsService;
import cn.x5456.leyou.goods.utils.ThreadUtils;
import cn.x5456.leyou.item.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsClient goodsClient;

    @Resource
    private BrandClient brandClient;

    @Resource
    private CategoryClient categoryClient;

    @Resource
    private SpecClient specClient;

    /**
     * 根据id查处商品的各种信息
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> loadModel(Long id) {

        // 1.根据id获取商品信息
            TbSpuEntity spuEntity = goodsClient.querySpuById(id);
        // 2.根据id获取商品详情信息
        TbSpuDetailEntity spuDetailBySpuId = goodsClient.findSpuDetailBySpuId(id);
        // 3.根据id获取sku们
        List<TbSkuEntity> tbSkuEntities = goodsClient.querySkuById(id);
        // 4.根据brandId获取品牌名称
        TbBrandEntity brand = brandClient.findBrandById(spuEntity.getBrandId());
        // 5.根据cid3获取分类信息，需要一个列表
        String[] categories = categoryClient.queryCategoryByCid3(spuEntity.getCid3()).split("/");
        // 6.根据cid3获取规格参数组和组下规格参数
        List<TbSpecGroupEntity> tbSpecGroupEntities = specClient.querySpecsByCid(spuEntity.getCid3());
//        // 7.查询特有参数（就是在页面筛选的字段），其实这些在第6步中已经包含了，但是让前端判断太残忍了
//        List<TbSpecParamEntity> genericParams = specClient.findAllByGenericAndCid(spuEntity.getCid3());

        Map<String, Object> map = new HashMap<>();
        map.put("spu", spuEntity);
        map.put("detail", spuDetailBySpuId);
        map.put("skus", tbSkuEntities);
        map.put("brand", brand);
        map.put("categories", categories);
        map.put("specs", tbSpecGroupEntities);
//        map.put("params", genericParams);


        return map;
    }


    @Resource
    private TemplateEngine templateEngine;

    /**
     * 创建html页面
     *
     * @param spuId
     * @throws Exception
     */
    public void createHtml(Long spuId) {

        PrintWriter writer = null;
        try {
            // 获取页面数据
            Map<String, Object> spuMap = this.loadModel(spuId);

            // 创建thymeleaf上下文对象
            Context context = new Context();
            // 把数据放入上下文对象
            context.setVariables(spuMap);

            // 创建输出流
            File file = new File("/usr/local/Cellar/nginx/1.15.5/html/item/" + spuId + ".html");
            writer = new PrintWriter(file);

            // 执行页面静态化方法
            templateEngine.process("item", context, writer);
        } catch (Exception e) {
            log.error("页面静态化出错：{}，"+ e, spuId);
            e.printStackTrace();
            throw new LyException(ExceptionEnums.PAGE_CONVERT_FAILD);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 删除相应的静态页面
     * @param id
     */
    @Override
    public void deleteHtml(Long id) {

    }

    /**
     * 新建线程处理页面静态化
     * @param spuId
     */
    public void asyncExcute(Long spuId) {
        ThreadUtils.execute(()->createHtml(spuId));
        /*ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                createHtml(spuId);
            }
        });*/
    }
}
