package it.lucianofilippucci.university.techbazaar.repositories.mongodb;

import it.lucianofilippucci.university.techbazaar.entities.mongodb.NotificationEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationEntity, ObjectId> {
    Optional<NotificationEntity> findNotificationEntitiesByUserId(int id);

    Optional<NotificationEntity> findByNotificationId(ObjectId id);

}
