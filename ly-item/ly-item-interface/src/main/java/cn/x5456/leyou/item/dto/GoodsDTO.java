package cn.x5456.leyou.item.dto;

import cn.x5456.leyou.item.entity.TbSkuEntity;
import cn.x5456.leyou.item.entity.TbSpuDetailEntity;
import cn.x5456.leyou.item.entity.TbSpuEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;


/**
 * 商品保存DTO
 * @author x5456
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDTO extends TbSpuEntity implements Serializable {

//    // 商品信息
//    private TbSpuEntity spuEntity;

    // 商品详细信息
    @Transient
    private TbSpuDetailEntity spuDetail;

    // sku信息
    @Transient
    private List<TbSkuEntity> skus;  // 库存也由它来接收

}
