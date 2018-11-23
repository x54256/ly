package cn.x5456.leyou.order.controller;

import cn.x5456.leyou.order.dto.OrderDTO;
import cn.x5456.leyou.order.entity.TbOrderEntity;
import cn.x5456.leyou.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 订单控制器
 */
@Slf4j
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     * @param orderDTO 部分订单信息
     * @return 订单编号
     */
    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody @Valid OrderDTO orderDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderDTO));
    }

    /**
     * 根据订单ID查询订单详情
     * @param orderId
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<TbOrderEntity> queryOrderById(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderService.queryById(orderId));
    }

}
