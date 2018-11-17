package cn.x5456.leyou.item.service.impl;

import cn.x5456.leyou.common.pojo.PageResult;
import cn.x5456.leyou.item.entity.TbBrandEntity;
import cn.x5456.leyou.item.repository.BrandRepository;
import cn.x5456.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;


    @Override
    public PageResult<TbBrandEntity> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key){


        /**
         * SpringBoot2.x版本的写法**
         *
         * arg1：第几页
         * arg2：每页显示几个
         * arg3：正序还是倒序
         * argsssss：按照哪些字段进行排序
         */
        Pageable pageable;
        if (desc != null) {
            if (desc) {
                pageable = PageRequest.of(page, rows, Sort.Direction.DESC, sortBy);
            } else {
                pageable = PageRequest.of(page, rows, Sort.Direction.ASC, sortBy);
            }
        }else{
            pageable = PageRequest.of(page, rows);
        }

        Page<TbBrandEntity> brandEntityPage = brandRepository.findByNameLike("%" + key + "%", pageable);

        return new PageResult<TbBrandEntity>(brandEntityPage.getTotalElements(),brandEntityPage.getTotalPages(),brandEntityPage.getContent());
//        return brandRepository.findBrandOfPage("%"+key+"%",pageable);
//        return brandRepository.findAll(new Specification<BrandEntity>() {
//
//            @Override
//            public Predicate toPredicate(Root<BrandEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
//                List<Predicate> predicate = new ArrayList<>();
//                if (!"".equals(key)) {
//                    predicate.add(cb.like(root.get("leyou"), "%" + key + "%"));
//                }
//                Predicate[] p = new Predicate[predicate.size()];
//                return cb.and(predicate.toArray(p));
//            }
//        }, pageable);
    }

    @Override
    public void addBrand(TbBrandEntity brand) {
        brandRepository.save(brand);
    }


    @Override
    public String queryBrandById(Long bid){

        return brandRepository.findById(bid).get().getName();

    }

    /**
     * 根据三级分类查询所有品牌
     * @param cid
     * @return
     */
    @Override
    public List<TbBrandEntity> findBrandsByCid3(Long cid) {

        List<Object[]> list = brandRepository.findAllBrandByCid3(cid);

        List<TbBrandEntity> brandEntities = new ArrayList<>();
        for (Object[] objects : list) {
            Long id = ((BigInteger)objects[0]).longValue();
            String name = (String) objects[1];
            TbBrandEntity tbBrandEntity = new TbBrandEntity(id, name);
            brandEntities.add(tbBrandEntity);
        }

        return brandEntities;
    }

    /**
     * 根据品牌id查询品牌
     * @param id
     * @return
     */
    @Override
    public TbBrandEntity findBrandById(Long id) {
        return brandRepository.findById(id).orElse(null);
    }


}
