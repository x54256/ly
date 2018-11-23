package cn.x5456.leyou.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 订单实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_order", schema = "leyou", catalog = "")
public class TbOrderEntity implements Serializable {

    @Id
    private Long orderId;   // 订单id

    private Long totalPay;  // 总金额，单位为分
    private Long actualPay; // 实付金额。单位:分。如:20007，表示:200元7分
    private String promotionIds;
    private Integer paymentType = 1;    // 支付类型，1、在线支付，2、货到付款
    private Long postFee = 0L;   // 邮费。单位:分。如:20007，表示:200元7分

    private Date createTime;    // 订单创建时间

    private String shippingName;    // 物流名称
    private String shippingCode;    // 物流单号

    private String userId;  // 用户id
    private String buyerMessage;    // 买家留言
    private String buyerNick;   // 买家昵称
    private Integer buyerRate = 0;  // 买家是否已经评价,0未评价，1已评价

    private String receiverState;   // 收货地址（省）
    private String receiverCity;    // 收货地址（市）
    private String receiverDistrict;    // 收货地址（区/县）
    private String receiverAddress; // 收货地址（街道、住址等详细地址）
    private String receiverMobile;  // 收货人手机
    private String receiverZip; // 收货人邮编
    private String receiver;    // 收货人

    private Integer invoiceType;    // 发票类型(0无发票1普通发票，2电子发票，3增值税发票)
    private Integer sourceType; // 订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端

    // 订单详情（商品那些）
    @Transient
    private List<TbOrderDetailEntity> orderDetails;

    // 订单状态信息
    @Transient
    private TbOrderStatusEntity orderStatus;

}
