package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.OrderEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.StoreOrderEntity;
import it.lucianofilippucci.university.techbazaar.helpers.Entities.Order;
import it.lucianofilippucci.university.techbazaar.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<Order> getAllUserOrder(@RequestParam("userId") int userId) {
        return orderService.getAllUserOrders(userId);
    }

    @PreAuthorize("hasRole('STORE')")
    @GetMapping("/store")
    public List<StoreOrderEntity> getAllStoreOrders(@RequestParam("storeId") int storeId) {
        return this.orderService.getStoreOrders(storeId);
    }
}
