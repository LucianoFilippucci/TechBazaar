package it.lucianofilippucci.university.techbazaar.entities;

import it.lucianofilippucci.university.techbazaar.helpers.model.AuctionStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "auction", schema = "techbazaar")
public class AuctionEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "auction_id", nullable = false)
    private int auctionId;


    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;


    @Basic
    @Column(name = "starting_price", nullable = false)
    private double startingPrice;

    @Basic
    @Column(name = "final_price")
    private float finalPrice;


    @ManyToOne
    @JoinColumn(name = "winner")
    private UserEntity winner;


    @Basic
    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Basic
    @Column(name = "final_date")
    private LocalDateTime finalDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AuctionStatus auctionStatus;

}
