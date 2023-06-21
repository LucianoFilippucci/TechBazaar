package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "order_status", schema = "techbazaar", catalog = "")
public class OrderStatusEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_status_type_id")
    private int orderStatusTypeId;
    @Basic
    @Column(name = "order_status_type")
    private String orderStatusType;
    @OneToMany(mappedBy = "orderStatusByOrderStatus")
    private Collection<OrderEntity> ordersByOrderStatusTypeId;

    public int getOrderStatusTypeId() {
        return orderStatusTypeId;
    }

    public void setOrderStatusTypeId(int orderStatusTypeId) {
        this.orderStatusTypeId = orderStatusTypeId;
    }

    public String getOrderStatusType() {
        return orderStatusType;
    }

    public void setOrderStatusType(String orderStatusType) {
        this.orderStatusType = orderStatusType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStatusEntity that = (OrderStatusEntity) o;
        return orderStatusTypeId == that.orderStatusTypeId && Objects.equals(orderStatusType, that.orderStatusType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderStatusTypeId, orderStatusType);
    }

    public Collection<OrderEntity> getOrdersByOrderStatusTypeId() {
        return ordersByOrderStatusTypeId;
    }

    public void setOrdersByOrderStatusTypeId(Collection<OrderEntity> ordersByOrderStatusTypeId) {
        this.ordersByOrderStatusTypeId = ordersByOrderStatusTypeId;
    }
}
