package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "order", schema = "techbazaar", catalog = "")
public class OrderEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_id")
    private int orderId;
    @Basic
    @Column(name = "order_date")
    private Timestamp orderDate;
    @Basic
    @Column(name = "order_status")
    private int orderStatus;
    @Basic
    @Column(name = "shipping_addr")
    private String shippingAddr;
    @Basic
    @Column(name = "order_total")
    private Double orderTotal;
    @Basic
    @Column(name = "contact_info")
    private String contactInfo;
    @Basic
    @Column(name = "user_note")
    private String userNote;
    @ManyToOne
    @JoinColumn(name = "order_status", referencedColumnName = "order_status_type_id", nullable = false)
    private OrderStatusEntity orderStatusByOrderStatus;
    @OneToMany(mappedBy = "orderByOrderId")
    private Collection<OrderDetailsEntity> orderDetailsByOrderId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getShippingAddr() {
        return shippingAddr;
    }

    public void setShippingAddr(String shippingAddr) {
        this.shippingAddr = shippingAddr;
    }

    public Double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return orderId == that.orderId && orderStatus == that.orderStatus && Objects.equals(orderDate, that.orderDate) && Objects.equals(shippingAddr, that.shippingAddr) && Objects.equals(orderTotal, that.orderTotal) && Objects.equals(contactInfo, that.contactInfo) && Objects.equals(userNote, that.userNote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderDate, orderStatus, shippingAddr, orderTotal, contactInfo, userNote);
    }

    public OrderStatusEntity getOrderStatusByOrderStatus() {
        return orderStatusByOrderStatus;
    }

    public void setOrderStatusByOrderStatus(OrderStatusEntity orderStatusByOrderStatus) {
        this.orderStatusByOrderStatus = orderStatusByOrderStatus;
    }

    public Collection<OrderDetailsEntity> getOrderDetailsByOrderId() {
        return orderDetailsByOrderId;
    }

    public void setOrderDetailsByOrderId(Collection<OrderDetailsEntity> orderDetailsByOrderId) {
        this.orderDetailsByOrderId = orderDetailsByOrderId;
    }
}
