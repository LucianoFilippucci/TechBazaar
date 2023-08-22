package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "auction", schema = "techbazaar", catalog = "")
public class AuctionEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @jakarta.persistence.Column(name = "auction_id")
    private int auctionId;

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    @Basic
    @Column(name = "product_id")
    private int productId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Basic
    @Column(name = "starting_price")
    private double startingPrice;

    public double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(double startingPrice) {
        this.startingPrice = startingPrice;
    }

    @Basic
    @Column(name = "winner")
    private String winner;

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    @Basic
    @Column(name = "date")
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionEntity that = (AuctionEntity) o;
        return auctionId == that.auctionId && productId == that.productId && Double.compare(startingPrice, that.startingPrice) == 0 && Objects.equals(winner, that.winner) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auctionId, productId, startingPrice, winner, date);
    }
}
