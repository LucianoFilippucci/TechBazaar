package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.AuctionEntity;
import it.lucianofilippucci.university.techbazaar.helpers.BidResponse;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.AuctionAlreadyStartedException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.BidLessThanCurrentException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.helpers.model.ResponseModel;
import it.lucianofilippucci.university.techbazaar.services.AuctionService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auction")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;


    @GetMapping("/")
    public ResponseEntity<ResponseModel> getAllAuction() {

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("All Auction")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of("auctions", auctionService.getAll()))
                        .build()
        );
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE')")
    @PostMapping("/new")
    public ResponseEntity<ResponseModel> newAuction(@RequestParam("productId") int productId, @RequestParam("startingPrice") float startingPrice, @RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") Date auctionDate, @RequestParam("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") Date endDate) {
        try {
            AuctionEntity auction = this.auctionService.newAuction(productId, startingPrice, auctionDate, endDate);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Auction Created.")
                            .status(HttpStatus.CREATED)
                            .statusCode(HttpStatus.CREATED.value())
                            .data(Map.of("auction", auction))
                            .build()
            );
        } catch(ObjectNotFoundException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .reason("Product Not Found.")
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .status(HttpStatus.NOT_FOUND)
                            .build()
            );
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE')")
    @PostMapping("/edit")
    public ResponseEntity<ResponseModel> editAuction(@RequestParam("auctionId") int auctionId, @RequestParam("productId") int productId, @RequestParam("startingPrice") float startingPrice, @RequestParam("date") Date auctionDate) {
        try {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Auction Edited Successfully.")
                            .statusCode(HttpStatus.OK.value())
                            .status(HttpStatus.OK)
                            .data(Map.of("auction", auctionService.editAuction(auctionId, productId, startingPrice, auctionDate)))
                            .build()
            );
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .reason("Product or Auction not Found.")
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .build()
            );
        } catch (AuctionAlreadyStartedException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .reason("Auction already Started.")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/bid")
    public ResponseEntity<ResponseModel> placeBid(@RequestParam("auctionId") int auctionId, @RequestParam("userId") int userId, @RequestParam("bidTime") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") Date bidTime, @RequestParam("price") float price) {
        try {
            BidResponse response = this.auctionService.placeBid(auctionId, userId, bidTime, price);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .statusCode(HttpStatus.OK.value())
                            .status(HttpStatus.OK)
                            .data(Map.of("bid", response))
                            .build()
            );
        } catch (ObjectNotFoundException e){
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .reason("Object Not Found.")
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .status(HttpStatus.NOT_FOUND)
                            .build()
            );
        } catch (BidLessThanCurrentException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .reason("Bid Less Than Current.")
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
            );
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    @PostMapping("/save")
    public ResponseEntity<ResponseModel> saveAuction(@RequestParam("userId") int userId, @RequestParam("auctionId") int auctionId) {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.NOT_IMPLEMENTED.value())
                        .status(HttpStatus.NOT_IMPLEMENTED)
                        .reason("Not Implemented")
                        .message("Not Implemented")
                        .build()
        );
    }

    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    @DeleteMapping("/remove")
    public ResponseEntity<ResponseModel> removeAuction(@RequestParam("userId") int userId, @RequestParam("auctionId") int auctionId) {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.NOT_IMPLEMENTED.value())
                        .status(HttpStatus.NOT_IMPLEMENTED)
                        .reason("Not Implemented")
                        .reason("Not Implemented")
                        .build()
        );
    }
}
