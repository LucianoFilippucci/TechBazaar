package it.lucianofilippucci.university.techbazaar.helpers;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {

    private float cartTotal;
    private float taxTotal;
    private List<ProductInCart> products;
}

