package cn.x5456.leyou.order.service.impl;

import cn.x5456.leyou.common.enums.ExceptionEnums;
import cn.x5456.leyou.common.exception.LyException;
import cn.x5456.leyou.common.utils.IdWorker;
import cn.x5456.leyou.item.entity.TbSkuEntity;
import cn.x5456.leyou.order.client.AddressClient;
import cn.x5456.leyou.order.client.GoodsClient;
import cn.x5456.leyou.order.dto.AddressDTO;
import cn.x5456.leyou.order.dto.OrderDTO;
import cn.x5456.leyou.order.entity.TbOrderDetailEntity;
import cn.x5456.leyou.order.entity.TbOrderEntity;
import cn.x5456.leyou.order.entity.TbOrderStatusEntity;
import cn.x5456.leyou.order.enums.OrderStatusEnum;
import cn.x5456.leyou.order.interceptor.LoginInterceptor;
import cn.x5456.leyou.order.repository.OrderDetailRepository;
import cn.x5456.leyou.order.repository.OrderRepository;
import cn.x5456.leyou.order.repository.OrderStatusRepository;
import cn.x5456.leyou.order.service.OrderService;
import cn.x5456.leyou.order.utils.PayHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private IdWorker idWorker;

    @Resource
    private GoodsClient goodsClient;

    /**
     * 生成订单
     * @param orderDTO
     * @return
     */
    @Transactional
    @Override
    public Long createOrder(@Valid OrderDTO orderDTO) {

        // 1.新增订单
        TbOrderEntity order = new TbOrderEntity();
        order.setCreateTime(new Date());

        // 1.1 订单编号（采用雪花算法）
        Long orderId = idWorker.nextId();
        order.setOrderId(orderId);
        // 1.2 用户信息
        order.setUserId(LoginInterceptor.getLoginUser().getId()+"");
        order.setBuyerNick(LoginInterceptor.getLoginUser().getUsername());
        // 1.3 收货人地址
        AddressDTO addr = AddressClient.findById(orderDTO.getAddressId());

        order.setReceiver(addr.getName());
        order.setReceiverAddress(addr.getAddress());
        order.setReceiverCity(addr.getCity());
        order.setReceiverState(addr.getState());
        order.setReceiverZip(addr.getZipCode());
        order.setReceiverDistrict(addr.getDistrict());
        order.setReceiverMobile(addr.getPhone());
        // 1.4 金额
        // 1.4.1 将购物车集合 ==> key:skuId，value:购买该sku数量
        Map<Long, Integer> map = orderDTO.getCarts().stream().collect(Collectors.toMap(x -> x.getSkuId(), x -> x.getNum()));

        Set<Long> keySet = map.keySet();
        Long[] ids = new Long[keySet.size()];
        keySet.toArray(ids);
        // 1.4.2 获取商品们的信息
        List<TbSkuEntity> skus = goodsClient.querySkuBySkuId(ids);
        // 1.4.3 计算价格
        Long totalPay = skus.stream().map(x -> x.getPrice() * map.get(x.getId())).reduce(Long::sum).orElse(0L);
        order.setTotalPay(totalPay + order.getPostFee());   // 商品+邮费
        order.setActualPay(totalPay + order.getPostFee() - 0L);   // 商品+邮费-优惠金额
        // 1.4.4 保存订单信息
        orderRepository.save(order).getOrderId();

        // 2.新增订单详情
        skus.forEach(x -> {
            TbOrderDetailEntity orderDetail = new TbOrderDetailEntity();
            orderDetail.setOrderId(orderId);
            orderDetail.setSkuId(x.getId());
            Integer num = map.get(x.getId());
            orderDetail.setNum(num);
            orderDetail.setTitle(x.getTitle());
            orderDetail.setOwnSpec(x.getOwnSpec());
            orderDetail.setPrice(num * x.getPrice());
            orderDetail.setImage(StringUtils.substringBefore(x.getImages(),","));
            // 2.1 保存
            orderDetailRepository.save(orderDetail);
        });

        // 3.新增订单状态
        TbOrderStatusEntity orderStatus = new TbOrderStatusEntity();

        orderStatus.setOrderId(orderId);
        orderStatus.setStatus(OrderStatusEnum.INIT.getCode());
        orderStatus.setCreateTime(new Date());
        // 3.1 保存
        orderStatusRepository.save(orderStatus);

        // 4.减库存（一定要放在最后，投机，避免分布式事务）
        goodsClient.decreaseStock(orderDTO.getCarts());

        // TODO: 2018/11/23 5.删除购物车中已经下单的商品数据, 采用异步mq的方式通知购物车系统删除已购买的商品，传送商品ID和用户ID


        return orderId;
    }


    @Override
    public TbOrderEntity queryById(Long orderId) {
        TbOrderEntity order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new LyException(ExceptionEnums.ORDER_NOT_FOUND);
        }

        List<TbOrderDetailEntity> orderDetails = orderDetailRepository.findAllByOrderId(orderId);
        order.setOrderDetails(orderDetails);

        TbOrderStatusEntity orderStatus = orderStatusRepository.findByOrderId(orderId);
        order.setOrderStatus(orderStatus);

        return order;
    }
}
