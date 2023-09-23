package it.lucianofilippucci.university.techbazaar.repositories;

import it.lucianofilippucci.university.techbazaar.entities.DailyOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DailyOfferRepository extends JpaRepository<DailyOfferEntity, Integer> {
    List<DailyOfferEntity> findDailyOfferEntitiesByStatus(String status);
    List<DailyOfferEntity> findDailyOfferEntitiesByDateAndStatus(LocalDateTime time, String status);
}
