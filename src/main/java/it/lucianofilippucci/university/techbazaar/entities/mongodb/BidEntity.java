package it.lucianofilippucci.university.techbazaar.entities.mongodb;

import it.lucianofilippucci.university.techbazaar.helpers.BidModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Document(collection = "bids")
public class BidEntity {
    // This class has all the bids for a certain Auction. Consider to Change className

    @Id
    private ObjectId bidId;

    private int auctionId;
    List<BidModel> bidsList;
}
