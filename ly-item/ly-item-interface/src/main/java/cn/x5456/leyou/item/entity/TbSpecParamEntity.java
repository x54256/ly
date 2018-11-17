package cn.x5456.leyou.item.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_spec_param", schema = "leyou", catalog = "")
public class TbSpecParamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cid;   // 商品分类id
    private Long groupId;   // 参数组id
    private String name;    // 参数名
    private Boolean numeric;   // 是否是数字类型参数，true或false。与segments参数有关联
    private String unit;    // 数字类型参数的单位，非数字类型可以为空
    private Boolean generic;   // 是否是sku通用属性（所有sku都一样的属性），true或false
    private Boolean searching; // 是否用于搜索过滤，true或false
    private String segments;    // 数值类型参数，如果需要搜索，则添加分段间隔值，如CPU频率间隔：0.5-1.0

}