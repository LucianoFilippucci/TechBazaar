package it.lucianofilippucci.university.techbazaar.repositories;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.entities.ProductReviewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReviewsRepository extends JpaRepository<ProductReviewsEntity, Integer> {
    ProductReviewsEntity findProductReviewsEntitiesByReviewId(int reviewId);
    @Query("SELECT ProductReviewsEntity FROM ProductReviewsEntity pre WHERE pre.product = :id")
    List<ProductReviewsEntity> findByProductId(@Param("id") ProductEntity pe);
}
