package cn.x5456.leyou.order.repository;

import cn.x5456.leyou.order.entity.TbOrderStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<TbOrderStatusEntity,Long> {
    TbOrderStatusEntity findByOrderId(Long orderId);
}
