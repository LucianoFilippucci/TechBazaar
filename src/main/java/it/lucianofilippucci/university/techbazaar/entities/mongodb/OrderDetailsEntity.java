package it.lucianofilippucci.university.techbazaar.entities.mongodb;

import it.lucianofilippucci.university.techbazaar.helpers.Entities.ProductInPurchase;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Document(collection = "order_details")
public class OrderDetailsEntity {

    @Id
    private String orderDetailIdentifier;

    private List<ProductInPurchase> productInPurchases;
    private float total;

    private int orderId;
}
