package it.lucianofilippucci.university.techbazaar.helpers.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductInCartModel {

    private int productId;
    private int qty;
    private float productPrice;
}
