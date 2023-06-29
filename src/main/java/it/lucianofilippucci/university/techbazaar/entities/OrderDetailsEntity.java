package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "order_details", schema = "techbazaar", catalog = "")
public class OrderDetailsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "detail_id")
    private int detailId;
    @Basic
    @Column(name = "order_id")
    private int orderId;
    @Basic
    @Column(name = "product_id")
    private int productId;
    @Basic
    @Column(name = "quantity")
    private int quantity;
    @Basic
    @Column(name = "unitary_price")
    private double unitaryPrice;
    @Basic
    @Column(name = "total")
    private Double total;
    @Basic
    @Column(name = "iva")
    private Integer iva;

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitaryPrice() {
        return unitaryPrice;
    }

    public void setUnitaryPrice(double unitaryPrice) {
        this.unitaryPrice = unitaryPrice;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getIva() {
        return iva;
    }

    public void setIva(Integer iva) {
        this.iva = iva;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderDetailsEntity that = (OrderDetailsEntity) o;

        if (detailId != that.detailId) return false;
        if (orderId != that.orderId) return false;
        if (productId != that.productId) return false;
        if (quantity != that.quantity) return false;
        if (Double.compare(that.unitaryPrice, unitaryPrice) != 0) return false;
        if (total != null ? !total.equals(that.total) : that.total != null) return false;
        if (iva != null ? !iva.equals(that.iva) : that.iva != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = detailId;
        result = 31 * result + orderId;
        result = 31 * result + productId;
        result = 31 * result + quantity;
        temp = Double.doubleToLongBits(unitaryPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (total != null ? total.hashCode() : 0);
        result = 31 * result + (iva != null ? iva.hashCode() : 0);
        return result;
    }
}
