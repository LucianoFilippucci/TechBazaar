package it.lucianofilippucci.university.techbazaar.repositories.mongodb;

import it.lucianofilippucci.university.techbazaar.entities.mongodb.BidEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BidRepository extends MongoRepository<BidEntity, Integer> {

    Optional<BidEntity> findByAuctionId(int auctionId);
}
