package it.lucianofilippucci.university.techbazaar.entities.mongodb;

import it.lucianofilippucci.university.techbazaar.helpers.Entities.ProductInPurchase;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Document(collection = "store_order_details")
public class StoreOrderEntity {
    @Id
    private String orderId;

    private List<ProductInPurchase> productInPurchases;
    private float total;

    private String userOrderId;

    private int storeId;
    private Date orderDate;
    private String userAddress;
}
