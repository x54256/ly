package cn.x5456.leyou.order.service;

import cn.x5456.leyou.order.dto.OrderDTO;
import cn.x5456.leyou.order.entity.TbOrderEntity;

import javax.validation.Valid;

public interface OrderService {
    Long createOrder(@Valid OrderDTO orderDTO);

    TbOrderEntity queryById(Long orderId);

}
