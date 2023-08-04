package it.lucianofilippucci.university.techbazaar.services.mongodb;

import it.lucianofilippucci.university.techbazaar.entities.mongodb.OrderDetailsEntity;
import it.lucianofilippucci.university.techbazaar.repositories.mongodb.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailsService {
    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    public OrderDetailsEntity getOrderDetail(String orderId) {
        return orderDetailsRepository.findByOrderId(orderId);
    }
}
