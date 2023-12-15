package it.lucianofilippucci.university.techbazaar.services;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.OrderDetailsEntity;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.UserNotAStoreException;
import it.lucianofilippucci.university.techbazaar.services.mongodb.OrderDetailsService;
import it.lucianofilippucci.university.techbazaar.services.mongodb.ReviewsLikedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UnifiedAccessService {

    private final OrderDetailsService orderDetailsService;

    private final ProductService productService;

    private final UserService userService;
    private final ReviewsLikedService reviewsLikedService;
    private NotificationService notificationService;    
    
    public void sendNotification(int from, int to, String subject, String message) throws ObjectNotFoundException {
        notificationService.sendMessage(from, to, subject, message);
    }

    public OrderDetailsEntity getOrderDetail(String orderId) {
        return orderDetailsService.getOrderDetail(orderId);
    }

    public Optional<UserEntity> getUserById(int id) {
        return userService.getById(id);
    }
    public ProductEntity getProduct(int productId) throws ObjectNotFoundException {
        return productService.getById(productId);
    }

    public UserEntity getStoreById(int storeId) throws ObjectNotFoundException, UserNotAStoreException {
        Optional<UserEntity> entity = userService.getById(storeId);
        if(entity.isPresent()) {
            if(entity.get().getPIva() > 0) {
                // Since a normal user do not have a P.iva  if it's > 0 this user is a Store.
                return entity.get();
            }
            throw new UserNotAStoreException();
        }
        throw new ObjectNotFoundException();
    }
}
