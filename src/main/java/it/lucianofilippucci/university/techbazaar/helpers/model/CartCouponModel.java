package it.lucianofilippucci.university.techbazaar.helpers.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CartCouponModel {
    private String couponCode;
    private int discount;
}
