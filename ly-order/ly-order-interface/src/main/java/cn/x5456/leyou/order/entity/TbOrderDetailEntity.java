package cn.x5456.leyou.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 订单详情实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_order_detail", schema = "leyou", catalog = "")
public class TbOrderDetailEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 订单详情id
    private Long orderId;   // 订单id
    private Long skuId; // sku商品id
    private Integer num;    // 购买数量
    private String title;   // 商品标题
    private String ownSpec; // 商品特殊规格键值集
    private Long price; // 价格,单位：分
    private String image;   // 商品图片

}
