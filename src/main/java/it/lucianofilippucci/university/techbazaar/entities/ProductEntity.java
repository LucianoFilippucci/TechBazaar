package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "product", schema = "techbazaar", catalog = "")
public class ProductEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "product_id")
    private int productId;
    @Basic
    @Column(name = "product_name")
    private String productName;
    @Basic
    @Column(name = "product_description")
    private String productDescription;
    @Basic
    @Column(name = "product_price")
    private BigDecimal productPrice;
    @Basic
    @Column(name = "product_quantity")
    private int productQuantity;
    @Basic
    @Column(name = "product_total_sold")
    private Integer productTotalSold;
    @OneToMany(mappedBy = "productByProductId")
    private Collection<AuctionEntity> auctionsByProductId;
    @OneToMany(mappedBy = "productByProductId")
    private Collection<DailyOfferEntity> dailyOffersByProductId;
    @OneToMany(mappedBy = "productByProductId")
    private Collection<OrderDetailsEntity> orderDetailsByProductId;
    @OneToOne(mappedBy = "productByProductId")
    private ProductResourcesEntity productResourcesByProductId;
    @OneToMany(mappedBy = "productByProductId")
    private Collection<ProductReviewsEntity> productReviewsByProductId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Integer getProductTotalSold() {
        return productTotalSold;
    }

    public void setProductTotalSold(Integer productTotalSold) {
        this.productTotalSold = productTotalSold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity that = (ProductEntity) o;
        return productId == that.productId && productQuantity == that.productQuantity && Objects.equals(productName, that.productName) && Objects.equals(productDescription, that.productDescription) && Objects.equals(productPrice, that.productPrice) && Objects.equals(productTotalSold, that.productTotalSold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, productDescription, productPrice, productQuantity, productTotalSold);
    }

    public Collection<AuctionEntity> getAuctionsByProductId() {
        return auctionsByProductId;
    }

    public void setAuctionsByProductId(Collection<AuctionEntity> auctionsByProductId) {
        this.auctionsByProductId = auctionsByProductId;
    }

    public Collection<DailyOfferEntity> getDailyOffersByProductId() {
        return dailyOffersByProductId;
    }

    public void setDailyOffersByProductId(Collection<DailyOfferEntity> dailyOffersByProductId) {
        this.dailyOffersByProductId = dailyOffersByProductId;
    }

    public Collection<OrderDetailsEntity> getOrderDetailsByProductId() {
        return orderDetailsByProductId;
    }

    public void setOrderDetailsByProductId(Collection<OrderDetailsEntity> orderDetailsByProductId) {
        this.orderDetailsByProductId = orderDetailsByProductId;
    }

    public ProductResourcesEntity getProductResourcesByProductId() {
        return productResourcesByProductId;
    }

    public void setProductResourcesByProductId(ProductResourcesEntity productResourcesByProductId) {
        this.productResourcesByProductId = productResourcesByProductId;
    }

    public Collection<ProductReviewsEntity> getProductReviewsByProductId() {
        return productReviewsByProductId;
    }

    public void setProductReviewsByProductId(Collection<ProductReviewsEntity> productReviewsByProductId) {
        this.productReviewsByProductId = productReviewsByProductId;
    }
}
