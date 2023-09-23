package it.lucianofilippucci.university.techbazaar.services;

import it.lucianofilippucci.university.techbazaar.entities.PriceVariationEntity;
import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.repositories.PriceVariationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PriceVariationService {

    PriceVariationRepository priceVariationRepository;

    public PriceVariationService(PriceVariationRepository priceVariationRepository) {
        this.priceVariationRepository = priceVariationRepository;
    }

    @Transactional
    public void newVariation(ProductEntity product, float newPrice, String status, String type) {
        PriceVariationEntity priceVariationEntity = new PriceVariationEntity();
        priceVariationEntity.setProduct(product);
        priceVariationEntity.setNewPrice(newPrice);
        priceVariationEntity.setType(type);
        priceVariationEntity.setStatus(status);
        this.priceVariationRepository.save(priceVariationEntity);
    }
}
