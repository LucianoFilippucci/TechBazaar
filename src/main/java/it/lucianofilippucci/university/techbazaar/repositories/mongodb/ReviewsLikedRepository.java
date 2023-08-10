package it.lucianofilippucci.university.techbazaar.repositories.mongodb;

import it.lucianofilippucci.university.techbazaar.entities.mongodb.ReviewsLiked;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewsLikedRepository extends MongoRepository<ReviewsLiked, String> {
    Optional<ReviewsLiked> findReviewsLikedByUserId(int userId);


}
