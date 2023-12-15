package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.entities.ProductReviewsEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.StoreNotFound;
import it.lucianofilippucci.university.techbazaar.helpers.model.ResponseModel;
import it.lucianofilippucci.university.techbazaar.services.ProductReviewService;
import it.lucianofilippucci.university.techbazaar.services.ProductService;
import it.lucianofilippucci.university.techbazaar.services.UnifiedAccessService;
import it.lucianofilippucci.university.techbazaar.services.UserService;
import it.lucianofilippucci.university.techbazaar.services.mongodb.ReviewsLikedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.hibernate.boot.model.internal.CreateKeySecondPass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/product/reviews")
@RequiredArgsConstructor
public class ProductReviewsController {

    ProductReviewService productReviewService;

    //ReviewsLikedService reviewsLikedService;

    //UserService userService;

    //ProductService productService;

    private final UnifiedAccessService unifiedAccessService;



    @GetMapping
    public ResponseEntity<ResponseModel> getAllReviews(@RequestParam("productId") int productId, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("sortBy") String sortBy) {
        if(sortBy.isEmpty())
            sortBy = "reviewId";

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of("reviews", productReviewService.getAllReviews(productId, page, pageSize, sortBy)))
                        .build()
        );

    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/check-exist")
    public ResponseEntity<ResponseModel> userReviewAlreadyExist(@RequestParam("userId") int userId, @RequestParam("productId") int productId) {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .data(Map.of("exists", productReviewService.userReviewExists(userId, productId)))
                        .build()
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-user-review")
    public ResponseEntity<ResponseModel> getUserReview(@RequestParam("reviewId") int reviewId) {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.NOT_IMPLEMENTED.value())
                        .status(HttpStatus.NOT_IMPLEMENTED)
                        .data(Map.of("review", productReviewService.getUserReview(reviewId)))
                        .reason("Implemented but to be modified")
                        .build()
        );
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/new")
    public ResponseEntity<ResponseModel> newReview(
            @RequestParam("productId") int productId,
            @RequestParam("userId") int userId,
            @RequestParam("starCount") int starCount,
            @RequestParam("title") String title,
            @RequestParam("body") String body) {

        HttpStatus status = null;
        String reason = "";

        try {
            if (productReviewService.newReview(productId, userId, starCount, title, body)) {
                status = HttpStatus.CREATED;
                reason = "Product Review published?.";
            } else {
                status = HttpStatus.BAD_REQUEST;
                reason = "Something went Wrong";
            }
        } catch(ObjectNotFoundException e) {
            status = HttpStatus.NOT_FOUND;
            reason = "User or Product not Found.";
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(status)
                        .statusCode(status.value())
                        .reason(reason)
                        .message(reason)
                        .build()
        );

    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/edit")
    public ResponseEntity<ResponseModel> editReview(@RequestParam("reviewId") int reviewId, @RequestParam("title") String title, @RequestParam("body") String body, @RequestParam("stars") int stars) {
        if(this.productReviewService.editReview(reviewId, title, body, stars))
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .message("Review Edited.")
                            .build()
            );
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .reason("Something went Wrong")
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/like")
    public ResponseEntity<ResponseModel> likeReview(@RequestParam("reviewId") int reviewId, @RequestParam("userId") int userId) {
        HttpStatus status = null;
        String reason = "";

        if (productReviewService.likeReview(reviewId, userId)) {
            status = HttpStatus.OK;
            reason = "Review Liked.";
        } else {
            status = HttpStatus.BAD_REQUEST;
            reason = "Something went wrong";
        }
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(status.value())
                        .status(status)
                        .reason(reason)
                        .message(reason)
                        .build()
        );
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/review/new/upload")
    public ResponseEntity<ResponseModel> uploadReviewFile(@PathVariable("id") int id, @RequestParam("files") MultipartFile[] files, @RequestParam("userId") int userId) {
//        ResponseMessage<String> response = new ResponseMessage<>("No files Uploaded.");
//        if(files.length > 0) {
//            try {
//                ProductEntity productEntity = productService.getById(id);
//                response = productReviewService.uploadFiles(files, id, productEntity.getStore().getUserId(), userId);
//
//            } catch(ObjectNotFoundException e ){
//                //TODO
//            }
//        }
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .status(HttpStatus.NOT_IMPLEMENTED)
                        .statusCode(HttpStatus.NOT_IMPLEMENTED.value())
                        .build()
        );
    }




}


