package it.lucianofilippucci.university.techbazaar.repositories.mongodb;

import it.lucianofilippucci.university.techbazaar.entities.mongodb.ProductResourceEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductResourcesRepository extends MongoRepository<ProductResourceEntity, ObjectId> {
    Optional<ProductResourceEntity> findByProductId(int productId);
}
