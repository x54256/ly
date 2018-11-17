package cn.x5456.leyou.item.repository;


import cn.x5456.leyou.item.entity.TbSpuEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface SpuRepository extends JpaRepository<TbSpuEntity,Long>, JpaSpecificationExecutor<TbSpuEntity> {


    @Query(value = "SELECT * FROM tb_spu WHERE saleable = ?1 AND title LIKE ?2",
            countQuery = "SELECT count(*) FROM tb_spu WHERE saleable = ?1 AND title LIKE ?2",
            nativeQuery = true)
    Page<TbSpuEntity> findSpuOfPage(Boolean saleable, String key, Pageable pageable);
}
