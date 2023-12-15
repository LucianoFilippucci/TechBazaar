package it.lucianofilippucci.university.techbazaar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

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

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity user;

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

    @Basic
    @Column(name = "date")
    private Date date;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private ProductEntity product;
}
