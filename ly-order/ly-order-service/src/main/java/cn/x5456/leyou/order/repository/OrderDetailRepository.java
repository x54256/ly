package cn.x5456.leyou.order.repository;

import cn.x5456.leyou.order.entity.TbOrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<TbOrderDetailEntity,Long> {

    List<TbOrderDetailEntity> findAllByOrderId(Long orderId);
}
