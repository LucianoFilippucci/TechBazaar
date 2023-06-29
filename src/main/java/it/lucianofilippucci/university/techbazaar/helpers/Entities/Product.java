package it.lucianofilippucci.university.techbazaar.helpers.Entities;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.helpers.Exceptions.ProductIdNotFound;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    private String storeId;

    public Product(ProductEntity entity) throws ProductIdNotFound {
        if(entity == null) throw new ProductIdNotFound(); // ????????????????
        this.id = entity.getId();
        this.name = entity.getProductName();
        this.description = entity.getProductDescription();
        this.category = "";
        this.price = (float) entity.getProductPrice();
        this.qty = entity.getProductQuantity();
        this.storeId = entity.getStoreIdentifier();
    }

    public Product() {
    }
}
