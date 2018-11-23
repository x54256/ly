package cn.x5456.leyou.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 订单状态表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_order_status", schema = "leyou", catalog = "")
public class TbOrderStatusEntity implements Serializable{

    @Id
    private Long orderId;   // 订单id
    private Integer status; // 状态：1、未付款 2、已付款,未发货 3、已发货,未确认 4、交易成功 5、交易关闭 6、已评价
    private Date createTime;    // 订单创建时间
    private Date paymentTime;   // 付款时间
    private Date consignTime;   // 发货时间
    private Date endTime;   // 交易完成时间
    private Date closeTime; // 交易关闭时间
    private Date commentTime;   // 评价时间

}
