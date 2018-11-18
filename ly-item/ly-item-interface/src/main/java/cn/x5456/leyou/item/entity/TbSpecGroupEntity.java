package cn.x5456.leyou.item.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_spec_group", schema = "leyou", catalog = "")
public class TbSpecGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cid;   // 商品分类id，一个分类下有多个规格组
    private String name;    // 规格组名称

    @Transient
    private List<TbSpecParamEntity> params; // 该组下的所有规格参数集合
}
