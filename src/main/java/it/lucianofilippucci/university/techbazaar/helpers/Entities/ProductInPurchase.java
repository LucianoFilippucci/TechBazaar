package it.lucianofilippucci.university.techbazaar.helpers.Entities;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductInPurchase {
    private int productId;
    private int productQuantity;
    private float unitaryPrice;
    private float totalProductPrice;
}
