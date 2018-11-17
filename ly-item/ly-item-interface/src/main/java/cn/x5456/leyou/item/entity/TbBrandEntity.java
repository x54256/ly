package cn.x5456.leyou.item.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Data
@Entity
@Table(name = "tb_brand")
public class TbBrandEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;// 品牌名称
    private String image;// 品牌图片
    private Character letter;


    public TbBrandEntity(Long id, String name, String image, Character letter) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.letter = letter;
    }

    public TbBrandEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TbBrandEntity() {
    }
}