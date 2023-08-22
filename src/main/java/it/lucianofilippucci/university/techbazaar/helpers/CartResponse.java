package it.lucianofilippucci.university.techbazaar.helpers;

import it.lucianofilippucci.university.techbazaar.entities.CouponEntity;
import lombok.*;

import java.util.HashMap;
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
    private float cartTotalAfterCoupon;
    private List<CouponEntity> couponList;
    private List<ProductInCart> products;
}

