package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "auction", schema = "techbazaar", catalog = "")
public class AuctionEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "auction_id")
    private int auctionId;
    @Basic
    @Column(name = "product_id")
    private int productId;
    @Basic
    @Column(name = "auction_starting_price")
    private BigDecimal auctionStartingPrice;
    @Basic
    @Column(name = "auction_winner")
    private String auctionWinner;
    @Basic
    @Column(name = "auction_date")
    private Timestamp auctionDate;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private ProductEntity productByProductId;
    @OneToMany(mappedBy = "auctionByAuctionId")
    private Collection<BidEntity> bidsByAuctionId;

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public BigDecimal getAuctionStartingPrice() {
        return auctionStartingPrice;
    }

    public void setAuctionStartingPrice(BigDecimal auctionStartingPrice) {
        this.auctionStartingPrice = auctionStartingPrice;
    }

    public String getAuctionWinner() {
        return auctionWinner;
    }

    public void setAuctionWinner(String auctionWinner) {
        this.auctionWinner = auctionWinner;
    }

    public Timestamp getAuctionDate() {
        return auctionDate;
    }

    public void setAuctionDate(Timestamp auctionDate) {
        this.auctionDate = auctionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionEntity that = (AuctionEntity) o;
        return auctionId == that.auctionId && productId == that.productId && Objects.equals(auctionStartingPrice, that.auctionStartingPrice) && Objects.equals(auctionWinner, that.auctionWinner) && Objects.equals(auctionDate, that.auctionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auctionId, productId, auctionStartingPrice, auctionWinner, auctionDate);
    }

    public ProductEntity getProductByProductId() {
        return productByProductId;
    }

    public void setProductByProductId(ProductEntity productByProductId) {
        this.productByProductId = productByProductId;
    }

    public Collection<BidEntity> getBidsByAuctionId() {
        return bidsByAuctionId;
    }

    public void setBidsByAuctionId(Collection<BidEntity> bidsByAuctionId) {
        this.bidsByAuctionId = bidsByAuctionId;
    }
}
