package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.OrderEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.StoreOrderEntity;
import it.lucianofilippucci.university.techbazaar.helpers.Entities.Order;
import it.lucianofilippucci.university.techbazaar.helpers.model.ResponseModel;
import it.lucianofilippucci.university.techbazaar.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<ResponseModel> getAllUserOrder(@RequestParam("userId") int userId) {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .data(Map.of("orders", orderService.getAllUserOrders(userId)))
                        .message("User Orders")
                        .build()
        );
    }

    @PreAuthorize("hasRole('STORE')")
    @GetMapping("/store")
    public ResponseEntity<ResponseModel> getAllStoreOrders(@RequestParam("storeId") int storeId) {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .data(Map.of("orders", orderService.getStoreOrders(storeId)))
                        .message("Store Orders")
                        .build()
        );
    }
}
