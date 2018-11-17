package cn.x5456.leyou.item.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "tb_order_status", schema = "leyou", catalog = "")
public class TbOrderStatusEntity {
    private long orderId;
    private Integer status;
    private Timestamp createTime;
    private Timestamp paymentTime;
    private Timestamp consignTime;
    private Timestamp endTime;
    private Timestamp closeTime;
    private Timestamp commentTime;

    @Id
    @Column(name = "order_id")
    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "payment_time")
    public Timestamp getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Timestamp paymentTime) {
        this.paymentTime = paymentTime;
    }

    @Basic
    @Column(name = "consign_time")
    public Timestamp getConsignTime() {
        return consignTime;
    }

    public void setConsignTime(Timestamp consignTime) {
        this.consignTime = consignTime;
    }

    @Basic
    @Column(name = "end_time")
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "close_time")
    public Timestamp getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Timestamp closeTime) {
        this.closeTime = closeTime;
    }

    @Basic
    @Column(name = "comment_time")
    public Timestamp getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Timestamp commentTime) {
        this.commentTime = commentTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbOrderStatusEntity that = (TbOrderStatusEntity) o;
        return orderId == that.orderId &&
                Objects.equals(status, that.status) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(paymentTime, that.paymentTime) &&
                Objects.equals(consignTime, that.consignTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(closeTime, that.closeTime) &&
                Objects.equals(commentTime, that.commentTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(orderId, status, createTime, paymentTime, consignTime, endTime, closeTime, commentTime);
    }
}
