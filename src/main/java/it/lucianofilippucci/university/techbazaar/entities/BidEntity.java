package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "bid", schema = "techbazaar", catalog = "")
public class BidEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "bid_id")
    private int bidId;
    @Basic
    @Column(name = "bid_user")
    private String bidUser;
    @Basic
    @Column(name = "auction_id")
    private int auctionId;
    @ManyToOne
    @JoinColumn(name = "auction_id", referencedColumnName = "auction_id", nullable = false)
    private AuctionEntity auctionByAuctionId;

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public String getBidUser() {
        return bidUser;
    }

    public void setBidUser(String bidUser) {
        this.bidUser = bidUser;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BidEntity bidEntity = (BidEntity) o;
        return bidId == bidEntity.bidId && auctionId == bidEntity.auctionId && Objects.equals(bidUser, bidEntity.bidUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bidId, bidUser, auctionId);
    }

    public AuctionEntity getAuctionByAuctionId() {
        return auctionByAuctionId;
    }

    public void setAuctionByAuctionId(AuctionEntity auctionByAuctionId) {
        this.auctionByAuctionId = auctionByAuctionId;
    }
}
