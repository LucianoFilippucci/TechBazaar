package it.lucianofilippucci.university.techbazaar.repositories;

import it.lucianofilippucci.university.techbazaar.entities.AuctionEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.helpers.model.AuctionStatus;
import it.lucianofilippucci.university.techbazaar.services.AuctionService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<AuctionEntity, Integer> {

    List<AuctionEntity> findAuctionEntitiesByAuctionStatus(AuctionStatus auctionStatus);
    List<AuctionEntity> findAuctionEntitiesByWinner(UserEntity user); // winner
}
