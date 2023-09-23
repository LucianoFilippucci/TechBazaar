package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.DailyOfferEntity;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.services.DailyOfferService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/daily")
public class DailyOfferController {

    DailyOfferService dailyOfferService;
    public DailyOfferController(DailyOfferService dailyOfferService) {
        this.dailyOfferService = dailyOfferService;
    }

    @GetMapping("/")
    public List<DailyOfferEntity> getTodayDaily() {
        return this.dailyOfferService.getDailyOffer();
    }

    @PreAuthorize("hasRole('STORE') or hasRole('ADMIN')")
    @PostMapping("/new")
    public ResponseEntity<ResponseMessage<String>> newDaily(@RequestParam("productId") int productId, @RequestParam("storeId") int storeId, @RequestParam("discount") int discount,  @RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") Date date) {
        if(this.dailyOfferService.newDailyOffer(productId, storeId, date, discount))
            return new ResponseEntity<>(new ResponseMessage<>("OK").setIsError(false), HttpStatus.OK);
        return  new ResponseEntity<>(new ResponseMessage<>("ERROR").setIsError(true), HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('STORE') or hasRole('ADMIN')")
    @PostMapping("/edit")
    public ResponseEntity<ResponseMessage<String>> editDaily(@RequestParam("dailyId") int dailyId, @RequestParam("storeId") int storeId, @RequestParam("discount") int discount,  @RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") Date date) {
        return null;
    }
}
