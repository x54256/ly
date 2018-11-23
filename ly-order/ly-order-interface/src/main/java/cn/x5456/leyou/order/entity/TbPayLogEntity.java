package cn.x5456.leyou.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 支付信息实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_pay_log", schema = "leyou", catalog = "")
public class TbPayLogEntity {
    @Id
    private Long orderId;   // 订单号
    private Long totalFee;  // 支付金额（分）
    private Long userId;    // 用户ID
    private String transactionId;   // 微信交易号码
    private String status; // 交易状态，1 未支付, 2已支付, 3 已退款, 4 支付错误, 5 已关闭
    private String payType;    // 支付方式，1 微信支付, 2 货到付款
    private String bankType;    // 银行类型
    private Date createTime;    // 创建时间
    private Date payTime;   // 支付时间
    private Date closedTime;    // 关闭时间
    private Date refundTime;    // 退款时间


}
