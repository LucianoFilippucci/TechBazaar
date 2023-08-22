package it.lucianofilippucci.university.techbazaar.repositories;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.entities.ProductReviewsEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductReviewsRepository extends JpaRepository<ProductReviewsEntity, Integer> {
    Optional<ProductReviewsEntity> findProductReviewsEntitiesByReviewId(int reviewId);
    @Query("SELECT pre FROM ProductReviewsEntity pre WHERE pre.product.productId = :id")
    Page<ProductReviewsEntity> findByProductId(@Param("id") int pid, Pageable paging);

    @Query("SELECT pre FROM ProductReviewsEntity  pre WHERE pre.user.userId = :id AND pre.product.productId = :productId")
    List<ProductReviewsEntity> existByUserId(@Param("id") int userId, @Param("productId") int productId);


}
