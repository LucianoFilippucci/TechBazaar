package it.lucianofilippucci.university.techbazaar.helpers;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.OrderDetailsEntity;
import it.lucianofilippucci.university.techbazaar.services.ProductService;
import it.lucianofilippucci.university.techbazaar.services.mongodb.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EverythingServices {
    @Autowired
    OrderDetailsService orderDetailsService;

    @Autowired
    ProductService productService;

    public OrderDetailsEntity getOrderDetail(String orderId) {
        return orderDetailsService.getOrderDetail(orderId);
    }

    public ProductEntity getProduct(int productId) {
        return productService.getById(productId);
    }
}
