package it.lucianofilippucci.university.techbazaar.services;

import it.lucianofilippucci.university.techbazaar.entities.AuctionEntity;
import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.BidEntity;
import it.lucianofilippucci.university.techbazaar.helpers.BidModel;
import it.lucianofilippucci.university.techbazaar.helpers.BidResponse;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.AuctionAlreadyStartedException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.BidLessThanCurrentException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.helpers.model.AuctionStatus;
import it.lucianofilippucci.university.techbazaar.repositories.AuctionRepository;
import it.lucianofilippucci.university.techbazaar.repositories.mongodb.BidRepository;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuctionService {

    private final Logger logger = LoggerFactory.getLogger(AuctionService.class);

    AuctionRepository auctionRepository;
    ProductService productService;
    BidRepository bidRepository;
    UserService userService;
    NotificationService notificationService;
    PriceVariationService priceVariationService;
    public AuctionService(PriceVariationService priceVariationService, AuctionRepository auctionRepository, ProductService productService, BidRepository bidRepository, UserService userService, NotificationService notificationService) {
        this.auctionRepository = auctionRepository;
        this.productService = productService;
        this.bidRepository = bidRepository;
        this.userService = userService;
        this.notificationService = notificationService;
        this.priceVariationService = priceVariationService;
    }
    public Collection<AuctionEntity> getAll() {
        return this.auctionRepository.findAll();
    }

    @Transactional
    public boolean saveAuction() {
        return true;
    }

    @Transactional
    public AuctionEntity newAuction(int productId, float startingPrice, Date auctionDate, Date auctionEnd) throws ObjectNotFoundException {
        Optional<ProductEntity> product = Optional.ofNullable(this.productService.getById(productId));

        if(product.isPresent()) {
            AuctionEntity auction = new AuctionEntity();
            auction.setProduct(product.get());
            auction.setDate(auctionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            auction.setFinalDate(auctionEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            auction.setStartingPrice(startingPrice);
            auction.setAuctionStatus(AuctionStatus.NOTSTARTED);
            AuctionEntity saved = this.auctionRepository.save(auction);

            BidEntity bidEntity = new BidEntity();
            bidEntity.setAuctionId(saved.getAuctionId());
            bidEntity.setBidsList(new ArrayList<>());
            this.bidRepository.save(bidEntity);

            return saved;
        } else throw new ObjectNotFoundException();
    }

    @Transactional
    public AuctionEntity editAuction(int auctionId, int productId, float startingPrice, Date auctionDate) throws ObjectNotFoundException, AuctionAlreadyStartedException {
        Optional<AuctionEntity> auction = this.auctionRepository.findById(auctionId);

        if(auction.isPresent()) {
            AuctionEntity entity = auction.get();


            LocalDateTime now = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime auctionStart = entity.getDate().atZone((ZoneId.systemDefault())).toLocalDateTime();
            if(now.isAfter(auctionStart)) throw new AuctionAlreadyStartedException();

            if(entity.getProduct().getProductId() != productId) {
                Optional<ProductEntity> productEntity = Optional.ofNullable(this.productService.getById(productId));
                if(productEntity.isEmpty()) throw new ObjectNotFoundException();
                entity.setProduct(productEntity.get());
            }
            if(!entity.getDate().isEqual(auctionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()))
                entity.setDate(auctionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            if(entity.getStartingPrice() != startingPrice)
                entity.setStartingPrice(startingPrice);

            return this.auctionRepository.save(entity);

        } else throw new ObjectNotFoundException();
    }

    @Transactional
    public BidResponse placeBid(int auctionId, int userId, Date bidTime, float price) throws ObjectNotFoundException, BidLessThanCurrentException {
        Optional<AuctionEntity> auction = this.auctionRepository.findById(auctionId);

        if(auction.isPresent()) {
            LocalDateTime endDate = auction.get().getFinalDate().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime now = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            logger.info("\n");
            logger.info("UserBidding.");
            logger.info("now: " + now);
            logger.info("end: " + endDate);
            logger.info("isAfter" + now.isAfter(endDate) + "\n");
            if(now.isAfter(endDate)) {
                Optional<BidEntity> auctionBids = this.bidRepository.findByAuctionId(auctionId);
                if(auctionBids.isPresent()) {
                    setWinnerAndNotify(auction.get());
                    return new BidResponse().setIsAccepted(false).setCause("Auction Closed.");
                } else throw new ObjectNotFoundException();
            } else {
                Optional<BidEntity> auctionBids = this.bidRepository.findByAuctionId(auctionId);
                if(auctionBids.isPresent()) {
                    if(price <= auction.get().getFinalPrice()) throw new BidLessThanCurrentException();
                    List<BidModel> auctionBidList = auctionBids.get().getBidsList();
                    auctionBidList.add(new BidModel(userId, bidTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), price));
                    auction.get().setFinalPrice(price);
                    this.auctionRepository.save(auction.get());
                    auctionBids.get().setBidsList(auctionBidList);
                    this.bidRepository.save(auctionBids.get());
                    return new BidResponse().setIsAccepted(true);
                } else throw new ObjectNotFoundException();
            }
        } else throw new ObjectNotFoundException();
    }


    @Scheduled(fixedRate = 60000)
    public void checkAuctions() throws ObjectNotFoundException{
        logger.info("Starting checkAuctions().");
        List<AuctionEntity> activeAuctions = this.auctionRepository.findAuctionEntitiesByAuctionStatus(AuctionStatus.ACTIVE);
        List<AuctionEntity> notStartedAuctions = this.auctionRepository.findAuctionEntitiesByAuctionStatus(AuctionStatus.NOTSTARTED);
        if(!activeAuctions.isEmpty()) {
            logger.info("Found" + activeAuctions.size() + " Active Auctions\n");
            for(AuctionEntity auction : activeAuctions) {
                LocalDateTime now = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime endDate = auction.getFinalDate().atZone(ZoneId.systemDefault()).toLocalDateTime();
                logger.info("now: " + now);
                logger.info("end: " + endDate);
                logger.info("isAfter: " + now.isAfter(endDate));
                if(now.isAfter(endDate)) {
                    setWinnerAndNotify(auction);
                }
            }
        }
        logger.info("Ended checking ACTIVE Auctions.\n");
        logger.info("Starting checking for NOT STARTED Auctions.");

        if(!notStartedAuctions.isEmpty()) {
            logger.info("Found [" + notStartedAuctions.size() + "] Auction that has to be started. ");
            for(AuctionEntity auction : notStartedAuctions) {
                LocalDateTime now = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime startTime = auction.getDate().atZone((ZoneId.systemDefault())).toLocalDateTime();

                logger.info("now: " + now);

                logger.info("startTime: " + startTime);

                logger.info("is after: " + now.isAfter(startTime));


                if(now.isAfter(startTime)) {
                    logger.info("Auction with id [" + auction.getAuctionId() + "] started.");
                    auction.setAuctionStatus(AuctionStatus.ACTIVE);
                    this.auctionRepository.save(auction);
                    //TODO: Notify Everyone who saved the auction (?)
                }
            }
        }
        logger.info("Ended checking for NOT STARTED Auctions\n");
    }

    private void setWinnerAndNotify(AuctionEntity auction) throws ObjectNotFoundException{
        Optional<BidEntity> auctionBids = this.bidRepository.findByAuctionId(auction.getAuctionId());
        if(auctionBids.isPresent()) {
            if(!auctionBids.get().getBidsList().isEmpty()) {
                BidModel winnerBid = auctionBids.get().getBidsList().get(auctionBids.get().getBidsList().size() - 1);
                Optional<UserEntity> winner = this.userService.getById(winnerBid.getUserId());
                if(winner.isEmpty()) throw new ObjectNotFoundException();
                auction.setWinner(winner.get());
                auction.setAuctionStatus(AuctionStatus.CLOSED);
                this.auctionRepository.save(auction);
                //winner.get().getAuctionsWon().add(auction);
                //this.userService.userRepository.save(winner.get());
                logger.info("User {" + winner.get().getUsername() + "} Won!");
                logger.info("Action with id [" + auction.getAuctionId() + "] closed.");

                this.priceVariationService.newVariation(auction.getProduct(), auction.getFinalPrice(), "PENDING", "AUCTION");
                List<UserEntity> notifiedUsers = new ArrayList<>();
                for(BidModel bid : auctionBids.get().getBidsList()) {
                    Optional<UserEntity> user = this.userService.getById(bid.getUserId());
                    //TODO: add winner there and not outside;
                    if(user.isEmpty()) throw new ObjectNotFoundException();
                    if(!notifiedUsers.contains(user.get())){
                        if(user.get().getUserId() == winner.get().getUserId()) {
                            this.notificationService.sendMessage(0, winnerBid.getUserId(), "YOU WON", "Congrats You won the Auction!");
                        } else {
                            this.notificationService.sendMessage(0, user.get().getUserId(), "YOU LOST", "Unfortunately you lost the Auction.");
                        }
                        notifiedUsers.add(user.get());
                    }
                    //winner.get().getAuctionsWon().add(auction);
                    //this.userService.userRepository.save(winner.get());
                }
            } else {
                logger.info("Auction closed without any bid");
                //TODO: notify To whoever made the auction that it closed without any bid
            }
            auction.setAuctionStatus(AuctionStatus.CLOSED);
            this.auctionRepository.save(auction);
        }
    }


}
