package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_details", schema = "techbazaar", catalog = "")
public class OrderDetailsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_details_id")
    private int orderDetailsId;
    @Basic
    @Column(name = "order_id")
    private int orderId;
    @Basic
    @Column(name = "product_id")
    private int productId;
    @Basic
    @Column(name = "order_product_quantity")
    private int orderProductQuantity;
    @Basic
    @Column(name = "order_product_unitary_price")
    private BigDecimal orderProductUnitaryPrice;
    @Basic
    @Column(name = "order_total")
    private BigDecimal orderTotal;
    @Basic
    @Column(name = "iva")
    private int iva;
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
    private OrderEntity orderByOrderId;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private ProductEntity productByProductId;

    public int getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailsId(int orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOrderProductQuantity() {
        return orderProductQuantity;
    }

    public void setOrderProductQuantity(int orderProductQuantity) {
        this.orderProductQuantity = orderProductQuantity;
    }

    public BigDecimal getOrderProductUnitaryPrice() {
        return orderProductUnitaryPrice;
    }

    public void setOrderProductUnitaryPrice(BigDecimal orderProductUnitaryPrice) {
        this.orderProductUnitaryPrice = orderProductUnitaryPrice;
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetailsEntity that = (OrderDetailsEntity) o;
        return orderDetailsId == that.orderDetailsId && orderId == that.orderId && productId == that.productId && orderProductQuantity == that.orderProductQuantity && iva == that.iva && Objects.equals(orderProductUnitaryPrice, that.orderProductUnitaryPrice) && Objects.equals(orderTotal, that.orderTotal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderDetailsId, orderId, productId, orderProductQuantity, orderProductUnitaryPrice, orderTotal, iva);
    }

    public OrderEntity getOrderByOrderId() {
        return orderByOrderId;
    }

    public void setOrderByOrderId(OrderEntity orderByOrderId) {
        this.orderByOrderId = orderByOrderId;
    }

    public ProductEntity getProductByProductId() {
        return productByProductId;
    }

    public void setProductByProductId(ProductEntity productByProductId) {
        this.productByProductId = productByProductId;
    }
}
