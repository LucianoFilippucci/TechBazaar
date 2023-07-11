package it.lucianofilippucci.university.techbazaar.services;

import it.lucianofilippucci.university.techbazaar.entities.StoreEntity;
import it.lucianofilippucci.university.techbazaar.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreService {
    @Autowired
    StoreRepository storeRepository;

    public StoreEntity getStoreById(String id) {
        return storeRepository.findByStoreId(id);
    }
}
