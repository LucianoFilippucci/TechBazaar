package it.lucianofilippucci.university.techbazaar.services;

import it.lucianofilippucci.university.techbazaar.entities.OrderEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.helpers.Helpers;
import it.lucianofilippucci.university.techbazaar.repositories.OrderRepository;
import it.lucianofilippucci.university.techbazaar.repositories.mongodb.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Transactional(readOnly = true)
    public List<OrderEntity> getAllUserOrders(int userId) {
        UserEntity entity = new UserEntity();
        entity.setUserId(userId);
        return orderRepository.findOrderEntitiesByUser(entity);
    }

    @Transactional
    public boolean updateOrderStatus(OrderEntity.OrderStatus newOrderStatus, String orderId) {
        Optional<OrderEntity> order = orderRepository.findById(orderId);
        if(order.isPresent()) {
            OrderEntity entity = order.get();
            entity.setOrderStatus(newOrderStatus);
            orderRepository.save(entity);
            Helpers.NotifyOrderStatusUpdate(entity.getUser());
            return true;
        }
        return false;
    }
}
