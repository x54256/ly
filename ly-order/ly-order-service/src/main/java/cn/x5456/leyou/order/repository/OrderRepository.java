package cn.x5456.leyou.order.repository;

import cn.x5456.leyou.order.entity.TbOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<TbOrderEntity,Long> {
}
