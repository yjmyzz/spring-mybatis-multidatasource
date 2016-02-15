package com.cnblogs.yjmyzz.entity;

import javax.persistence.*;

@Table(name = "T_ORDER")
public class OrderEntity {
    @Id
    @Column(name = "D_ORDER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @Column(name = "D_ORDER_NO")
    private String orderNo;

    /**
     * @return D_ORDER_ID
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * @param orderId
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * @return D_ORDER_NO
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * @param orderNo
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public OrderEntity(String orderNo) {
        this.orderNo = orderNo;
    }

    public OrderEntity() {
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "orderId=" + orderId +
                ", orderNo='" + orderNo + '\'' +
                '}';
    }
}