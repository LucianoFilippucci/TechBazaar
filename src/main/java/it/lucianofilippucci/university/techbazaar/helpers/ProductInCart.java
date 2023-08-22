package it.lucianofilippucci.university.techbazaar.helpers;


import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductInCart {
    private int productId;
    private float productPrice;
    private String productName;
    private int productQty;
    private String category;
    //TODO: add imgs
}
