package it.lucianofilippucci.university.techbazaar.repositories.mongodb;

import it.lucianofilippucci.university.techbazaar.entities.mongodb.WishListEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistRepository extends MongoRepository<WishListEntity, ObjectId> {

    Optional<WishListEntity> findByUserId(int userId);
}
