package it.lucianofilippucci.university.techbazaar.services;

import it.lucianofilippucci.university.techbazaar.entities.DailyOfferEntity;
import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.helpers.Entities.ProductReview;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.repositories.DailyOfferRepository;
import it.lucianofilippucci.university.techbazaar.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DailyOfferService {

    DailyOfferRepository dailyOfferRepository;

    ProductRepository productRepository;

    public DailyOfferService(DailyOfferRepository dailyOfferRepository, ProductRepository productRepository) {
        this.dailyOfferRepository = dailyOfferRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public boolean newDailyOffer(int productId, int storeId, Date date, int discount) {
        DailyOfferEntity dailyOfferEntity = new DailyOfferEntity();
        Optional<ProductEntity> product = this.productRepository.findByProductId(productId);
        if(product.isPresent()) {
            dailyOfferEntity.setProduct(product.get());
            dailyOfferEntity.setDiscount(discount);
            dailyOfferEntity.setDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            dailyOfferEntity.setStatus("NOTSTARTED");
            this.dailyOfferRepository.save(dailyOfferEntity);
            return true;
        }
        return false;
    }

    public List<DailyOfferEntity> getDailyOffer() {
        LocalDateTime localDateTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return this.dailyOfferRepository.findDailyOfferEntitiesByDateAndStatus(localDateTime,"ACTIVE");
    }
}
