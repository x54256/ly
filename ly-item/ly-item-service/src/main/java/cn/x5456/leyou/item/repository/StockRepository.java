package cn.x5456.leyou.item.repository;


import cn.x5456.leyou.item.entity.TbStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StockRepository extends JpaRepository<TbStockEntity,Long> {

    @Modifying
    @Query("update TbStockEntity s set s.stock = s.stock - ?2 where s.skuId = ?1 and s.stock >= ?2")
    Integer decreaseStock(Long skuId, Integer num);

}
