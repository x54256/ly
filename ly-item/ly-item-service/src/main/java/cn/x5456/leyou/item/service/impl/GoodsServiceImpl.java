package cn.x5456.leyou.item.service.impl;

import cn.x5456.leyou.common.enums.ExceptionEnums;
import cn.x5456.leyou.common.exception.LyException;
import cn.x5456.leyou.common.pojo.PageResult;
import cn.x5456.leyou.item.dozer.EJBGenerator;
import cn.x5456.leyou.item.dto.GoodsDTO;
import cn.x5456.leyou.item.entity.TbSkuEntity;
import cn.x5456.leyou.item.entity.TbSpuDetailEntity;
import cn.x5456.leyou.item.entity.TbSpuEntity;
import cn.x5456.leyou.item.entity.TbStockEntity;
import cn.x5456.leyou.item.repository.SkuRepository;
import cn.x5456.leyou.item.repository.SpuDetailRepository;
import cn.x5456.leyou.item.repository.SpuRepository;
import cn.x5456.leyou.item.repository.StockRepository;
import cn.x5456.leyou.item.service.BrandService;
import cn.x5456.leyou.item.service.CategoryService;
import cn.x5456.leyou.item.service.GoodsService;
import cn.x5456.leyou.order.dto.CartDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class  GoodsServiceImpl implements GoodsService {

    @Autowired
    private SpuRepository spuRepository;

    @Autowired
    private SpuDetailRepository spuDetailRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private StockRepository stockRepository;

    /**
     * 分页查询商品信息
     * @param page
     * @param rows
     * @param saleable:是否上架
     * @param key
     * @return
     */
    public PageResult<TbSpuEntity> querySpuByPageAndSort(Integer page, Integer rows, Boolean saleable, String key) {

        Pageable pageable = PageRequest.of(page, rows);

        Page<TbSpuEntity> spuOfPage = spuRepository.findSpuOfPage(saleable, "%" + key + "%", pageable);

        spuOfPage.getContent().forEach(x -> {

            // 获取分类名 ==> 家用电器/大 家 电/平板电视
            x.setCname(categoryService.queryCategoryByCid3(x.getCid3()));
            // 获取品牌名
            x.setBname(brandService.queryBrandById(x.getBrandId()));

        });

        return new PageResult<TbSpuEntity>(spuOfPage.getTotalElements(),spuOfPage.getTotalPages(),spuOfPage.getContent());
    }

    @Autowired
    protected EJBGenerator dozer;

    /**
     * 新增商品DTO
     * @param goodsDTO:商品DTO
     * @return
     */
    @Transactional
    @Override
    public void saveGoods(GoodsDTO goodsDTO) {

        // 1. 保存spu
        goodsDTO.setValid(true);
        goodsDTO.setSaleable(true);
        goodsDTO.setCreateTime(new Date());

        // 使用dozer进行优化
        // TbSpuEntity spu =  new TbSpuEntity(goodsDTO.getId(),goodsDTO.getBrandId(),goodsDTO.getCid1(),goodsDTO.getCid2(),goodsDTO.getCid3(),goodsDTO.getTitle(),goodsDTO.getSubTitle(),true,true,new Date(),new Date(),goodsDTO.getCname(),goodsDTO.getBname());
        TbSpuEntity spu = dozer.convert(goodsDTO, TbSpuEntity.class);

        Long spuId = spuRepository.save(spu).getId();
        // 2. 获取spu的id，保存spuDetail
        TbSpuDetailEntity spuDetailEntity = goodsDTO.getSpuDetail();
        spuDetailEntity.setSpuId(spuId);
        spuDetailRepository.save(spuDetailEntity);
        // 3. 保存所有sku
        List<TbSkuEntity> skuEntities = goodsDTO.getSkus();
            // 3.1 所有sku
            // 3.2 将库存字段取出，保存
        skuEntities.forEach(x -> {
            x.setCreateTime(new Date());
            x.setLastUpdateTime(new Date());
            x.setSpuId(spuId);
            Long skuId = skuRepository.save(x).getId();
            TbStockEntity tbStockEntity = new TbStockEntity(skuId, null, null, x.getStock());
            stockRepository.save(tbStockEntity);
        });

        // 4.发送消息，实现模板的生成和索引库的增加
        sendMessage(spuId,"insert");

    }

    @Transactional
    @Override
    public void updateGoods(GoodsDTO goodsDTO) {

        // 1. 更新spu，使用的也是save方法
        TbSpuEntity spu = dozer.convert(goodsDTO, TbSpuEntity.class);
        spuRepository.save(spu);

        // 2. 获取spu的id，保存spuDetail
        TbSpuDetailEntity spuDetailEntity = goodsDTO.getSpuDetail();
        spuDetailRepository.save(spuDetailEntity);

        // 3.删除所有spu相关的sku
        skuRepository.deleteBySpuId(goodsDTO.getId());

        // 4. 保存所有sku
        List<TbSkuEntity> skuEntities = goodsDTO.getSkus();
        // 4.1 所有sku
        // 4.2 将库存字段取出，保存
        skuEntities.forEach(x -> {
            x.setLastUpdateTime(new Date());
            Long skuId = skuRepository.save(x).getId();
            TbStockEntity tbStockEntity = new TbStockEntity(skuId, null, null, x.getStock());
            stockRepository.save(tbStockEntity);
        });
    }

    /**
     * 根据id查询商品通用信息
     * @param id
     * @return
     */
    @Override
    public TbSpuEntity querySpuById(Long id) {

        Optional<TbSpuEntity> optional = spuRepository.findById(id);

        return optional.orElse(null);
    }

    /**
     * 根据spuid查询商品具体信息
     * @param spuId
     * @return
     */
    @Override
    public List<TbSkuEntity> querySkuById(Long spuId) {
        return skuRepository.findAllBySpuId(spuId);
    }

    /**
     * 查询所有spu
     * @return
     */
    @Override
    public List<TbSpuEntity> findAllSpu() {
        return spuRepository.findAll();
    }

    /**
     * spu详情信息
     */
    @Override
    public TbSpuDetailEntity querySpuDetailBySpuId(Long spuId) {
        return spuDetailRepository.findById(spuId).orElse(null);
    }

    /**
     * sku
     * @param skuIds
     * @return
     */
    @Override
    public List<TbSkuEntity> querySkuBySkuId(Long[] skuIds) {
        ArrayList<TbSkuEntity> list = new ArrayList<>();
        for (Long skuId : skuIds) {
            TbSkuEntity tbSkuEntity = skuRepository.findById(skuId).orElse(null);
            list.add(tbSkuEntity);
        }
        return list;
    }

    /**
     * 减少商品库存
     * @param skuId:商品id
     * @param num:购买数量
     * @return
     */
    @Transactional
    @Override
    public void decreaseStock(List<CartDTO> cartDTOS) {

        cartDTOS.forEach(x -> {
            // 返回的是更新数据的条数
            Integer integer = stockRepository.decreaseStock(x.getSkuId(), x.getNum());
            if (integer == 0){
                throw new LyException(ExceptionEnums.STOCK_IS_NOT_ENOUGH);
            }
        });
    }

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 封装发送消息的方法
     *  注意：这里要把所有异常都try起来，不能让消息的发送影响到正常的业务逻辑
     * @param id
     * @param type
     */
    private void sendMessage(Long id, String type){
        // 发送消息
        try {
            this.amqpTemplate.convertAndSend("item." + type, id);   // 这里没有指定交换机，因此默认发送到了配置中的：`leyou.item.exchange`
            log.info("消息发送成功，id为{}",id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}商品消息发送异常，商品id：{}", type, id, e);
        }
    }


}
