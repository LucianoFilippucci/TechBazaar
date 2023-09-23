package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "price_variation")
public class PriceVariationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variation_id")
    private int variationId;

    @Basic
    @Column(name = "new_price")
    private float newPrice;

    @Basic
    @Column(name = "status")
    private String status; // REJECTED, FULFILLED, CANCELED, PENDING

    @Basic
    @Column(name = "type")
    private String type; // AUCTION, DAILY

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

}
