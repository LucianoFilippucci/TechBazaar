package it.lucianofilippucci.university.techbazaar.services;

import it.lucianofilippucci.university.techbazaar.entities.OrderEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.OrderDetailsEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.StoreOrderEntity;
import it.lucianofilippucci.university.techbazaar.helpers.Entities.Order;
import it.lucianofilippucci.university.techbazaar.helpers.Entities.Product;
import it.lucianofilippucci.university.techbazaar.helpers.Entities.ProductInPurchase;
import it.lucianofilippucci.university.techbazaar.helpers.Helpers;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.repositories.OrderRepository;
import it.lucianofilippucci.university.techbazaar.repositories.mongodb.OrderDetailsRepository;
import it.lucianofilippucci.university.techbazaar.repositories.mongodb.StoreOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Autowired
    UnifiedAccessService unifiedAccessService;

    @Autowired
    StoreOrderRepository storeOrderRepository;

    @Transactional(readOnly = true)
    public List<Order> getAllUserOrders(int userId) {
        UserEntity entity = new UserEntity();
        entity.setUserId(userId);
        List<OrderEntity> list =  orderRepository.findOrderEntitiesByUser(entity);

        LinkedList<Order> orderList = new LinkedList<>();

        for(OrderEntity order : list) {
            OrderDetailsEntity detail = unifiedAccessService.getOrderDetail(order.getOrderId());
            if(detail != null) {

                LinkedList<Product> productInList = new LinkedList<>();
                // Aggiungere Qualcosa per mostrare i dati di ProductInPurchase
                for(ProductInPurchase pip : detail.getProductInPurchases()) {
                    try {
                        Product p = new Product(unifiedAccessService.getProduct(pip.getProductId()));
                        p.setQty(pip.getProductQuantity());
                        p.setPrice(pip.getUnitaryPrice());
                        productInList.add(p);
                    } catch( ObjectNotFoundException e) {
                        return orderList;
                        //TODO: wrap inside ResponseModel Object
                    }
                }
                Date today = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(today);

                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(order.getDate());
                calendar1.add(Calendar.DAY_OF_MONTH, 31);
                System.out.println(calendar);
                System.out.println(calendar1);

                Order o = new Order(
                        order.getOrderId(),
                        order.getDate(),
                        order.getShippingAddr(),
                        order.getOrderTotal(),
                        order.getOrderStatus(),
                        order.getTrackingCode(),
                        order.getExpressCourier(),
                        productInList,
                        !calendar.after(calendar1)
                );
                orderList.add(o);
            }

        }
        return orderList;
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

    public List<StoreOrderEntity> getStoreOrders(int storeId) {
        return this.storeOrderRepository.findStoreOrderEntitiesByStoreId(storeId);
    }
}
