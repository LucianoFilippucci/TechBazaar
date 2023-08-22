package it.lucianofilippucci.university.techbazaar.repositories;

import it.lucianofilippucci.university.techbazaar.entities.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CouponRepository extends JpaRepository<CouponEntity, String> {

    @Query("SELECT ce FROM CouponEntity ce WHERE ce.store.userId = :id")
    List<CouponEntity> findAllByStore(@Param("id") int storeId);

}
