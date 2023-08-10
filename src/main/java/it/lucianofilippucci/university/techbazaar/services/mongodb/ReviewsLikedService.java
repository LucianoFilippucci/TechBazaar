package it.lucianofilippucci.university.techbazaar.services.mongodb;

import it.lucianofilippucci.university.techbazaar.entities.mongodb.ReviewsLiked;
import it.lucianofilippucci.university.techbazaar.repositories.mongodb.ReviewsLikedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewsLikedService {
    @Autowired
    ReviewsLikedRepository reviewsLikedRepository;

    public boolean likeReview(int userId, int productId) {
        Optional<ReviewsLiked> reviewsLiked = reviewsLikedRepository.findReviewsLikedByUserId(userId);

        if(reviewsLiked.isPresent()) {
            ReviewsLiked entity = reviewsLiked.get();

            for(Integer id : entity.getProductId()) {
                if(id == productId) {
                    entity.getProductId().remove(id);
                    reviewsLikedRepository.save(entity);
                    return true;
                }
            }
            entity.getProductId().add(productId);
            reviewsLikedRepository.save(entity);
            return true;
        }

        ReviewsLiked newUser = new ReviewsLiked();
        newUser.setUserId(userId);
        List<Integer> prodIds = new ArrayList<>();
        prodIds.add(productId);
        newUser.setProductId(prodIds);
        reviewsLikedRepository.save(newUser);
        return true;
    }
}
