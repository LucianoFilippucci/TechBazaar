package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "product_reviews", schema = "techbazaar")
public class ProductReviewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private int reviewId;

    @Basic
    @Column(name = "user_id", nullable = true)
    private int userId;

    @Basic
    @Column(name = "star_count", nullable = false)
    private int starCount;

    @Basic
    @Column(name = "title", nullable = false, length = 45)
    private String title;

    @Basic
    @Column(name = "body", nullable = false)
    private String body;

    @Basic
    @Column(name = "likes")
    private int likes;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}
