package cn.x5456.leyou.item.repository;


import cn.x5456.leyou.item.entity.TbStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<TbStockEntity,Long> {

}
