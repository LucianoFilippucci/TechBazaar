package it.lucianofilippucci.university.techbazaar.repositories;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    List<ProductEntity> findByProductNameContaining(String name);
    List<ProductEntity> findByProductId(Integer id);

}
