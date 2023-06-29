package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;

import java.sql.Date;

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
    @Column(name = "starting_price")
    private double startingPrice;
    @Basic
    @Column(name = "winner")
    private String winner;
    @Basic
    @Column(name = "date")
    private Date date;

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

    public double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

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

        if (auctionId != that.auctionId) return false;
        if (productId != that.productId) return false;
        if (Double.compare(that.startingPrice, startingPrice) != 0) return false;
        if (winner != null ? !winner.equals(that.winner) : that.winner != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = auctionId;
        result = 31 * result + productId;
        temp = Double.doubleToLongBits(startingPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (winner != null ? winner.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
