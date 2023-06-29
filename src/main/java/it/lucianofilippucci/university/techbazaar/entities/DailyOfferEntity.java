package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "daily_offer", schema = "techbazaar", catalog = "")
public class DailyOfferEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "daily_id")
    private int dailyId;
    @Basic
    @Column(name = "product_id")
    private int productId;
    @Basic
    @Column(name = "discount")
    private int discount;
    @Basic
    @Column(name = "date")
    private Date date;

    public int getDailyId() {
        return dailyId;
    }

    public void setDailyId(int dailyId) {
        this.dailyId = dailyId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DailyOfferEntity that = (DailyOfferEntity) o;

        if (dailyId != that.dailyId) return false;
        if (productId != that.productId) return false;
        if (discount != that.discount) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dailyId;
        result = 31 * result + productId;
        result = 31 * result + discount;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
