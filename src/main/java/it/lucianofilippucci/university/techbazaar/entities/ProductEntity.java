package it.lucianofilippucci.university.techbazaar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "product", schema = "techbazaar")
@JsonIgnoreProperties({"store", "reviews"})
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int productId;

    @Basic
    @Column(name = "product_name", nullable = false, length = 45)
    private String productName;

    @Basic
    @Column(name = "product_description", nullable = false, length = 255)
    private String productDescription;

    @Basic
    @Column(name = "category", nullable = true, length = 45)
    private String productCategory;

    @Basic
    @Column(name = "product_price", nullable = false)
    private float productPrice;

    @Basic
    @Column(name = "product_quantity", nullable = false)
    private int productQuantity;

    @Basic
    @Column(name = "product_total_selt", nullable = false)
    private int productTotalSelt;

    @ManyToOne
    @JoinColumn(name = "store_identifier")
    @JsonIgnore
    private UserEntity store;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<ProductReviewsEntity> reviews;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private Collection<DailyOfferEntity> dailyOffers;

//    @OneToMany
//    @JsonIgnore
//    private Collection<OrderDetailsEntity> orderDetailsEntities;
}
