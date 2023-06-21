package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "product_reviews", schema = "techbazaar", catalog = "")
public class ProductReviewsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "review_id")
    private int reviewId;
    @Basic
    @Column(name = "product_id")
    private int productId;
    @Basic
    @Column(name = "review_ratings")
    private int reviewRatings;
    @Basic
    @Column(name = "review_title")
    private String reviewTitle;
    @Basic
    @Column(name = "review_body")
    private String reviewBody;
    @Basic
    @Column(name = "review_like_counter")
    private Integer reviewLikeCounter;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private ProductEntity productByProductId;

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getReviewRatings() {
        return reviewRatings;
    }

    public void setReviewRatings(int reviewRatings) {
        this.reviewRatings = reviewRatings;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public String getReviewBody() {
        return reviewBody;
    }

    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }

    public Integer getReviewLikeCounter() {
        return reviewLikeCounter;
    }

    public void setReviewLikeCounter(Integer reviewLikeCounter) {
        this.reviewLikeCounter = reviewLikeCounter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductReviewsEntity that = (ProductReviewsEntity) o;
        return reviewId == that.reviewId && productId == that.productId && reviewRatings == that.reviewRatings && Objects.equals(reviewTitle, that.reviewTitle) && Objects.equals(reviewBody, that.reviewBody) && Objects.equals(reviewLikeCounter, that.reviewLikeCounter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId, productId, reviewRatings, reviewTitle, reviewBody, reviewLikeCounter);
    }

    public ProductEntity getProductByProductId() {
        return productByProductId;
    }

    public void setProductByProductId(ProductEntity productByProductId) {
        this.productByProductId = productByProductId;
    }
}
