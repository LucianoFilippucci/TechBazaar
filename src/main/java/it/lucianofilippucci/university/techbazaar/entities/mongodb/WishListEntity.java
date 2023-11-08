package it.lucianofilippucci.university.techbazaar.entities.mongodb;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Document(collection = "wishlist")
public class WishListEntity {

    @MongoId
    private ObjectId wishlistId;
    private int userId;
    private List<Integer> products;
}
