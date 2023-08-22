package it.lucianofilippucci.university.techbazaar.repositories.mongodb;

import it.lucianofilippucci.university.techbazaar.entities.mongodb.StoreOrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreOrderRepository extends MongoRepository<StoreOrderEntity, String> {
    Optional<StoreOrderEntity> findStoreOrderEntityByOrderId(String orderId);

    Optional<StoreOrderEntity> findStoreOrderEntityByUserOrderId(String userOrderId);

    List<StoreOrderEntity> findStoreOrderEntitiesByStoreId(int storeId);
}
