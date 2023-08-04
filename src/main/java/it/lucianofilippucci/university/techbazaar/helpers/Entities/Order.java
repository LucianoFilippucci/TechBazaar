package it.lucianofilippucci.university.techbazaar.helpers.Entities;

import it.lucianofilippucci.university.techbazaar.entities.OrderEntity;


import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Order {

    private String orderId;
    private Date date;
    private String shippingAddr;
    private float orderTotal;
    private OrderEntity.OrderStatus orderStatus;
    private String trackingCode;
    private String expressCourier;
    private List<Product> products;

    private boolean canBeReturned;


}
