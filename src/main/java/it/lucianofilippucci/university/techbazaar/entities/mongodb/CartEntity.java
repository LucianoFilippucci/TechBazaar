package it.lucianofilippucci.university.techbazaar.entities.mongodb;

import it.lucianofilippucci.university.techbazaar.entities.CouponEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Document(collection = "cart")
public class CartEntity {
    @Id
    private String cartId;

    private Date createdAt;
    private Date updatedAt;

    // The first Integer is the product id while the second Integer Value it's for the product qty.
    private HashMap<Integer, Integer> productsInCart;

    // String = CouponCode; Integer = Discount
    private HashMap<String, Integer> coupons;

}
