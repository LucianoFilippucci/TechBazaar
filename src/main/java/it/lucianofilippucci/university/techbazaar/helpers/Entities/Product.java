package it.lucianofilippucci.university.techbazaar.helpers.Entities;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.entities.ProductReviewsEntity;
import it.lucianofilippucci.university.techbazaar.entities.StoreEntity;
import it.lucianofilippucci.university.techbazaar.helpers.Exceptions.ProductIdNotFound;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Product {
    private int id;
    private String name;
    private String description;
    private String category;
    private float price;
    private int qty;
    private StoreEntity store;
    private int productQty; //needed only in Order.


    public Product(ProductEntity entity) {
        this.id = entity.getProductId();
        this.name = entity.getProductName();
        this.description = entity.getProductDescription();
        this.category = "";
        this.price = (float) entity.getProductPrice();
        this.qty = entity.getProductQuantity();
        this.store = entity.getStore();
    }

    public Product() {
    }
}
