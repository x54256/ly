package cn.x5456.leyou.item.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tb_order_detail", schema = "leyou", catalog = "")
public class TbOrderDetailEntity {
    private long id;
    private long orderId;
    private long skuId;
    private int num;
    private String title;
    private String ownSpec;
    private long price;
    private String image;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "order_id")
    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "sku_id")
    public long getSkuId() {
        return skuId;
    }

    public void setSkuId(long skuId) {
        this.skuId = skuId;
    }

    @Basic
    @Column(name = "num")
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "own_spec")
    public String getOwnSpec() {
        return ownSpec;
    }

    public void setOwnSpec(String ownSpec) {
        this.ownSpec = ownSpec;
    }

    @Basic
    @Column(name = "price")
    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Basic
    @Column(name = "image")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbOrderDetailEntity that = (TbOrderDetailEntity) o;
        return id == that.id &&
                orderId == that.orderId &&
                skuId == that.skuId &&
                num == that.num &&
                price == that.price &&
                Objects.equals(title, that.title) &&
                Objects.equals(ownSpec, that.ownSpec) &&
                Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, orderId, skuId, num, title, ownSpec, price, image);
    }
}
