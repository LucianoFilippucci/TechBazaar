package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.DailyOfferEntity;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.model.ResponseModel;
import it.lucianofilippucci.university.techbazaar.services.DailyOfferService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/daily")
@RequiredArgsConstructor
public class DailyOfferController {

    private final DailyOfferService dailyOfferService;

    @GetMapping("/")
    public ResponseEntity<ResponseModel> getTodayDaily() {

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Daily Offers")
                        .data(Map.of("offers", dailyOfferService.getDailyOffer()))
                        .build()
        );
    }

    @PreAuthorize("hasRole('STORE') or hasRole('ADMIN')")
    @PostMapping("/new")
    public ResponseEntity<ResponseModel> newDaily(@RequestParam("productId") int productId, @RequestParam("storeId") int storeId, @RequestParam("discount") int discount,  @RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") Date date) {
        if(this.dailyOfferService.newDailyOffer(productId, storeId, date, discount))
            return ResponseEntity.ok(ResponseModel.builder()
                    .timeStamp(LocalDateTime.now())
                    .message("Daily Offer Created")
                    .status(HttpStatus.CREATED)
                    .statusCode(HttpStatus.CREATED.value())
                    .build()
            );
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST)
                        .reason("IDK")
                        .build()
        );
    }

    @PreAuthorize("hasRole('STORE') or hasRole('ADMIN')")
    @PostMapping("/edit")
    public ResponseEntity<ResponseModel> editDaily(@RequestParam("dailyId") int dailyId, @RequestParam("storeId") int storeId, @RequestParam("discount") int discount,  @RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") Date date) {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.NOT_IMPLEMENTED.value())
                        .status(HttpStatus.NOT_IMPLEMENTED)
                        .reason("NOT IMPLEMENTED")
                        .build()
        );
    }
}
