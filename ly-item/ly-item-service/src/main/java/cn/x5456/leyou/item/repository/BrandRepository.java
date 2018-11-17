package cn.x5456.leyou.item.repository;


import cn.x5456.leyou.item.entity.TbBrandEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrandRepository extends JpaRepository<TbBrandEntity,Long>, JpaSpecificationExecutor<TbBrandEntity> {


    @Query(value = "SELECT * FROM tb_brand WHERE name LIKE ?1",
        countQuery = "SELECT count(*) FROM tb_brand WHERE name LIKE ?1",
        nativeQuery = true)
    Page<TbBrandEntity> findBrandOfPage(String keyword, Pageable pageable);


    Page<TbBrandEntity> findByNameLike(String keyword, Pageable pageable);


    /**
     * 需要新建一个实体，来保存这两个字段 例如：==> List<TbBrandDTO>
     * 此处就暂时使用List<Object[]>来接收了
     * @param cid3
     * @return
     */
    @Query(value = "SELECT t3.id,t3.name FROM tb_category t1 INNER JOIN tb_category_brand t2 on t1.id = t2.category_id INNER JOIN tb_brand t3 on t2.brand_id = t3.id where t1.id = ?1",
    nativeQuery = true)
    List<Object[]> findAllBrandByCid3(Long cid3);

}
