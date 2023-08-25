package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.AuctionEntity;
import it.lucianofilippucci.university.techbazaar.helpers.BidResponse;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.AuctionAlreadyStartedException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.BidLessThanCurrentException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.services.AuctionService;
import org.apache.coyote.Response;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/auction")
public class AuctionController {

    AuctionService auctionService;
    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping("/")
    public List<AuctionEntity> getAllAuction() {
        return this.auctionService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE')")
    @PostMapping("/new")
    public ResponseEntity<ResponseMessage<String>> newAuction(@RequestParam("productId") int productId, @RequestParam("startingPrice") float startingPrice, @RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") Date auctionDate, @RequestParam("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") Date endDate) {
        try {
            if(this.auctionService.newAuction(productId, startingPrice, auctionDate, endDate))
                return new ResponseEntity<>(new ResponseMessage<>("OK").setIsError(false), HttpStatus.OK);
            return new ResponseEntity<>(new ResponseMessage<>("GenericError").setIsError(true), HttpStatus.BAD_REQUEST);
        } catch(ObjectNotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage<>("ProductNotFound").setIsError(true), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE')")
    @PostMapping("/edit")
    public ResponseEntity<ResponseMessage<String>> editAuction(@RequestParam("auctionId") int auctionId, @RequestParam("productId") int productId, @RequestParam("startingPrice") float startingPrice, @RequestParam("date") Date auctionDate) {
        try {
            if(this.auctionService.editAuction(auctionId, productId, startingPrice, auctionDate))
                return new ResponseEntity<>(new ResponseMessage<>("OK").setIsError(false), HttpStatus.OK);
            return new ResponseEntity<>(new ResponseMessage<>("GenericError").setIsError(true), HttpStatus.BAD_REQUEST);
        } catch (ObjectNotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage<>("ObjectNotFound").setIsError(true), HttpStatus.NOT_FOUND);
        } catch (AuctionAlreadyStartedException e) {
            return new ResponseEntity<>(new ResponseMessage<>("AuctionAlreadyStarted").setIsError(true), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/bid")
    public ResponseEntity<ResponseMessage<BidResponse>> placeBid(@RequestParam("auctionId") int auctionId, @RequestParam("userId") int userId, @RequestParam("bidTime") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") Date bidTime, @RequestParam("price") float price) {
        try {
            BidResponse response = this.auctionService.placeBid(auctionId, userId, bidTime, price);
            return new ResponseEntity<>(new ResponseMessage<>(response).setIsError(false), HttpStatus.OK);
        } catch (ObjectNotFoundException e){
            return new ResponseEntity<>(new ResponseMessage<>(new BidResponse().setErrorMessage("ObjectNotFound")).setIsError(true), HttpStatus.BAD_REQUEST);
        } catch (BidLessThanCurrentException e) {
            return new ResponseEntity<>(new ResponseMessage<>(new BidResponse().setErrorMessage("BidLessThanCurrent")).setIsError(true), HttpStatus.BAD_REQUEST);
        }
    }
}
