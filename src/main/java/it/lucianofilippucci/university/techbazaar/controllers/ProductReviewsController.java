package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.ProductReviewsEntity;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.services.ProductReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product/{id}/reviews")
public class ProductReviewsController {

    @Autowired
    ProductReviewService productReviewService;

    @GetMapping
    public List<ProductReviewsEntity> getAllReviews(@PathVariable("id") int productId) {
        return productReviewService.getAllReviews(productId);
    }

    @PostMapping("/new")
    public ResponseEntity<ResponseMessage<String>> newReview(@RequestBody @Valid ProductReviewsEntity productReviewsEntity) {

        if(!productReviewService.newReview(productReviewsEntity))
            return new ResponseEntity<>(new ResponseMessage<>("Everything fine!").setIsError(false), HttpStatus.OK);
        return new ResponseEntity<>(new ResponseMessage<>("Something Failed").setIsError(true), HttpStatus.BAD_REQUEST);
    }

}


