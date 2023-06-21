package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "daily_offer", schema = "techbazaar", catalog = "")
public class DailyOfferEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "daily_offer_id")
    private int dailyOfferId;
    @Basic
    @Column(name = "product_id")
    private int productId;
    @Basic
    @Column(name = "offer_discount")
    private int offerDiscount;
    @Basic
    @Column(name = "offer_date")
    private Timestamp offerDate;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private ProductEntity productByProductId;

    public int getDailyOfferId() {
        return dailyOfferId;
    }

    public void setDailyOfferId(int dailyOfferId) {
        this.dailyOfferId = dailyOfferId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOfferDiscount() {
        return offerDiscount;
    }

    public void setOfferDiscount(int offerDiscount) {
        this.offerDiscount = offerDiscount;
    }

    public Timestamp getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(Timestamp offerDate) {
        this.offerDate = offerDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyOfferEntity that = (DailyOfferEntity) o;
        return dailyOfferId == that.dailyOfferId && productId == that.productId && offerDiscount == that.offerDiscount && Objects.equals(offerDate, that.offerDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dailyOfferId, productId, offerDiscount, offerDate);
    }

    public ProductEntity getProductByProductId() {
        return productByProductId;
    }

    public void setProductByProductId(ProductEntity productByProductId) {
        this.productByProductId = productByProductId;
    }
}
