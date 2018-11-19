package cn.x5456.leyou.search.service.impl;

import cn.x5456.leyou.common.pojo.PageResult;
import cn.x5456.leyou.common.utils.JsonUtils;
import cn.x5456.leyou.common.utils.NumberUtils;
import cn.x5456.leyou.item.entity.TbSkuEntity;
import cn.x5456.leyou.item.entity.TbSpecParamEntity;
import cn.x5456.leyou.item.entity.TbSpuDetailEntity;
import cn.x5456.leyou.item.entity.TbSpuEntity;
import cn.x5456.leyou.search.client.BrandClient;
import cn.x5456.leyou.search.client.CategoryClient;
import cn.x5456.leyou.search.client.GoodsClient;
import cn.x5456.leyou.search.client.SpecClient;
import cn.x5456.leyou.search.pojo.Goods;
import cn.x5456.leyou.search.pojo.SearchRequest;
import cn.x5456.leyou.search.repository.GoodsRepository;
import cn.x5456.leyou.search.service.SearchService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    @Resource
    private GoodsClient goodsClient;

    @Resource
    private CategoryClient categoryClient;

    @Resource
    private BrandClient brandClient;

    @Resource
    private SpecClient specClient;

    /*
    @Id
    private Long id; // spuId
    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;// 卖点
    private Long brandId;// 品牌id
    private Long cid1;// 1级分类id
    private Long cid2;// 2级分类id
    private Long cid3;// 3级分类id
    private Date createTime;// 创建时间


    private List<Long> price;// 价格
    @Field(type = FieldType.Keyword, index = false)
    private String skus;// sku信息的json结构


    private Map<String, Object> specs;// 可搜索的规格参数，key是参数名，值是参数值


    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String all; // 所有需要被搜索的信息，包含标题，分类，甚至品牌
     */

    /**
     * 将所有商品 ==> GoodsPOJO
     * @return
     */
    @Override
    public List<Goods> findAllGoodsList(){

        // 1.查询所有的spu
        List<TbSpuEntity> allSpu = goodsClient.findAllSpu();

        List<Goods> goodsList = new ArrayList<>();

        allSpu.forEach(x -> {
            Goods goods = this.buildGoods(x);
           goodsList.add(goods);
        });

        return goodsList;
    }

    /**
     * spu转goods
     * @param x
     * @return
     */
    private Goods buildGoods(TbSpuEntity x) {

        Goods goods = new Goods();
        goods.setId(x.getId());
        goods.setSubTitle(x.getSubTitle());
        goods.setBrandId(x.getBrandId());
        goods.setCid1(x.getCid1());
        goods.setCid2(x.getCid2());
        goods.setCid3(x.getCid3());
        goods.setCreateTime(x.getCreateTime());
        // 2.查询spu下的所有sku
        List<TbSkuEntity> skus = goodsClient.querySkuById(x.getId());
        goods.setPrice(getAllPrice(skus));
        goods.setSkus(JsonUtils.toString(skus));
        // 3.根据spu的分类id，找出需要搜索的规格参数
        // 3.1 查出所有需要搜索的规格参数
        List<TbSpecParamEntity> specBySearchingAndCid = specClient.findAllBySearchingAndCid(x.getCid3());
        // 3.2 查出商品详情，下的特有+共有的规格参数列表
        TbSpuDetailEntity spuDetailBySpuId = goodsClient.findSpuDetailBySpuId(x.getId());
        // 3.3 调用方法，返回字典
        goods.setSpecs(getSpecsMap(specBySearchingAndCid,spuDetailBySpuId));

        // 4.all ==> 标题，分类，品牌
        goods.setAll(x.getTitle() + " " + categoryClient.queryCategoryByCid3(x.getCid3()) + " " + brandClient.findBrandById(x.getBrandId()).getName());

        return goods;
    }

    /**
     *
     * @param specs：商品分类下需要检索的字段
     * @param spuDetail：商品详情信息
     * @return
     */
    private Map<String, Object> getSpecsMap(List<TbSpecParamEntity> specs, TbSpuDetailEntity spuDetail) {

        // todo：由于一些商品是段那种的区间，查询起来相对比较复杂，所以我们在存入索引库的时候就将它存成段的格式
        Map<String, Object> map = new HashMap<>();

        // 1.解析规格参数的json数据
            // 1.1 获取通用规格参数
        Map<Long, String> genericSpec = JsonUtils.nativeRead(spuDetail.getGenericSpec(), new TypeReference<Map<Long, String>>() {
        });
            // 1.2 获取特殊规格参数
        Map<Long, List<String>> specialSpec = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });

        // TODO: 2018/11/17 由于数据的原因，会报空指针异常
        for (TbSpecParamEntity x : specs) {
            String key = x.getName();
            Object value;

            if (x.getGeneric()){    // 判断这个参数是否是通用属性
                value = genericSpec.get(x.getId()); // 取出，赋值给value
                if (value == null) continue;
                if (x.getNumeric()){    // 判断这个参数是否为数值类型，如果是，则有段，我们需要将具体的数值转换成 ==》 区间
                    value = chooseSegment(value.toString(),x);
                }
            } else {
                value = specialSpec.get(x.getId());
                if (value == null) continue;
            }

            map.put(key,value);
        }

        return map;
    }

    // 获取spu的所有价格
    private List<Long> getAllPrice(List<TbSkuEntity> skus) {

        return skus.stream().map(x -> x.getPrice()).collect(Collectors.toList());
    }


    private String chooseSegment(String value, TbSpecParamEntity p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * 搜索功能
     * @param request
     * @return
     */
    @Override
    public PageResult<Goods> searchGoods(SearchRequest request) {
        String key = request.getKey();
        // 判断是否有搜索条件，如果没有，直接返回null。不允许搜索全部商品
        if (StringUtils.isBlank(key)) {
            return null;
        }

        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // 1、对key进行全文检索查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("all", key).operator(Operator.AND));

        // 2、通过sourceFilter设置返回的结果字段,我们只需要id、skus、subTitle
        queryBuilder.withSourceFilter(new FetchSourceFilter(
                new String[]{"id","skus","subTitle"}, null));

        // 3、分页
        // 准备分页参数
        int page = request.getPage();
        int size = request.getSize();
        queryBuilder.withPageable(PageRequest.of(page - 1, size));

        // 4、查询，获取结果
        Page<Goods> goodsPage = this.goodsRepository.search(queryBuilder.build());

        // 封装结果并返回
        return new PageResult<>(goodsPage.getTotalElements(), goodsPage.getTotalPages(), goodsPage.getContent());
    }

    @Override
    public void createIndex(Long id) throws IOException {

        TbSpuEntity spu = this.goodsClient.querySpuById(id);
        // 构建商品
        Goods goods = this.buildGoods(spu);

        // 保存数据到索引库
        this.goodsRepository.save(goods);
    }



    @Override
    public void deleteIndex(Long id) {
        this.goodsRepository.deleteById(id);
    }
}
