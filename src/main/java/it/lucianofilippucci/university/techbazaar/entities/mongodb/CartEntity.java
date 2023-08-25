package it.lucianofilippucci.university.techbazaar.entities.mongodb;

import it.lucianofilippucci.university.techbazaar.entities.CouponEntity;
import it.lucianofilippucci.university.techbazaar.helpers.model.CartCouponModel;
import it.lucianofilippucci.university.techbazaar.helpers.model.ProductInCartModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    private List<ProductInCartModel> productsInCart;

    private List<CartCouponModel> coupons;

}
