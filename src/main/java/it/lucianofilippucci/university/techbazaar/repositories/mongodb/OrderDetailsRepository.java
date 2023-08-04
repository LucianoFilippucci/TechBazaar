package it.lucianofilippucci.university.techbazaar.repositories.mongodb;

import it.lucianofilippucci.university.techbazaar.entities.mongodb.OrderDetailsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailsRepository extends MongoRepository<OrderDetailsEntity, String> {

    @Query("{'orderId': ?0}")
    OrderDetailsEntity findByOrderId(String orderId);
}
