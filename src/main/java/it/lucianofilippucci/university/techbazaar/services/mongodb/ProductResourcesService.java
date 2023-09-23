package it.lucianofilippucci.university.techbazaar.services.mongodb;

import it.lucianofilippucci.university.techbazaar.entities.mongodb.ProductResourceEntity;
import it.lucianofilippucci.university.techbazaar.repositories.mongodb.ProductResourcesRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductResourcesService {

    ProductResourcesRepository productResourcesRepository;
    public ProductResourcesService(ProductResourcesRepository productResourcesRepository) {
        this.productResourcesRepository = productResourcesRepository;
    }

    // Product Images by store
    @Transactional
    public boolean newResource(List<String> paths, int productId) {
        Optional<ProductResourceEntity> entityOptional = this.productResourcesRepository.findByProductId(productId);
        if(entityOptional.isEmpty()) return false;
        entityOptional.get().setProductId(productId);
        entityOptional.get().setProductImages(paths);
        this.productResourcesRepository.save(entityOptional.get());
        return true;
    }

    // Review Images by user

    @Transactional
    public boolean newResource(List<String> paths, int productId, int userId) {
        Optional<ProductResourceEntity> entityOptional = this.productResourcesRepository.findByProductId(productId);
        if(entityOptional.isEmpty()) return false;
        entityOptional.get().setReviewImages(paths, userId);
        this.productResourcesRepository.save(entityOptional.get());
        return true;
    }

    // new Resource in toto
    @Transactional
    public boolean newResource(int productId) {
        ProductResourceEntity entity = new ProductResourceEntity();
        entity.setProductId(productId);
        entity.setReviewImages(new ArrayList<>());
        entity.setProductImages(new ArrayList<>());
        return this.productResourcesRepository.save(entity).getProductId() > 0;
    }
}
