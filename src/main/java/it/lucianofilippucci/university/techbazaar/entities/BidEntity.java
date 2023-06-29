package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "bid", schema = "techbazaar", catalog = "")
public class BidEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "bid_id")
    private int bidId;
    @Basic
    @Column(name = "auction_id")
    private int auctionId;
    @Basic
    @Column(name = "bid_value")
    private double bidValue;
    @Basic
    @Column(name = "user")
    private String user;

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    public double getBidValue() {
        return bidValue;
    }

    public void setBidValue(double bidValue) {
        this.bidValue = bidValue;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BidEntity bidEntity = (BidEntity) o;

        if (bidId != bidEntity.bidId) return false;
        if (auctionId != bidEntity.auctionId) return false;
        if (Double.compare(bidEntity.bidValue, bidValue) != 0) return false;
        if (user != null ? !user.equals(bidEntity.user) : bidEntity.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = bidId;
        result = 31 * result + auctionId;
        temp = Double.doubleToLongBits(bidValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
