package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.entities.ProductReviewsEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.StoreNotFound;
import it.lucianofilippucci.university.techbazaar.services.ProductReviewService;
import it.lucianofilippucci.university.techbazaar.services.ProductService;
import it.lucianofilippucci.university.techbazaar.services.UserService;
import it.lucianofilippucci.university.techbazaar.services.mongodb.ReviewsLikedService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.hibernate.boot.model.internal.CreateKeySecondPass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product/reviews")
public class ProductReviewsController {

    ProductReviewService productReviewService;

    ReviewsLikedService reviewsLikedService;

    UserService userService;

    ProductService productService;

    public ProductReviewsController(ProductReviewService productReviewService, ReviewsLikedService reviewsLikedService, UserService userService, ProductService productService) {
        this.productReviewService = productReviewService;
        this.reviewsLikedService = reviewsLikedService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping
    public List<ProductReviewsEntity> getAllReviews(@RequestParam("productId") int productId, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("sortBy") String sortBy) {
        if(sortBy.isEmpty())
            sortBy = "reviewId";
        return productReviewService.getAllReviews(productId, page, pageSize, sortBy);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/check-exist")
    public ResponseEntity<Boolean> userReviewAlreadyExist(@RequestParam("userId") int userId, @RequestParam("productId") int productId) {
        return new ResponseEntity<>(productReviewService.userReviewExists(userId, productId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-user-review")
    public ResponseEntity getUserReview(@RequestParam("reviewId") int reviewId) {
        return this.productReviewService.getUserReview(reviewId);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/new")
    public ResponseEntity newReview(
            @RequestParam("productId") int productId,
            @RequestParam("userId") int userId,
            @RequestParam("starCount") int starCount,
            @RequestParam("title") String title,
            @RequestParam("body") String body) {

        Optional<UserEntity> user = userService.getById(userId);
        if(user.isPresent()) {
            ProductReviewsEntity pre = new ProductReviewsEntity();
            ProductEntity pe = productService.getById(productId);
            pre.setProduct(pe);
            pre.setTitle(title);
            pre.setBody(body);
            pre.setUser(user.get());
            pre.setStarCount(starCount);
            pre.setDate(new Date());

            if(productReviewService.newReview(pre))
                return new ResponseEntity(HttpStatus.OK);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/edit")
    public ResponseEntity<Boolean> editReview(@RequestParam("reviewId") int reviewId, @RequestParam("title") String title, @RequestParam("body") String body, @RequestParam("stars") int stars) {
        if(this.productReviewService.editReview(reviewId, title, body, stars))
            return new ResponseEntity<>(true, HttpStatus.OK);
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/like")
    public ResponseEntity<Boolean> likeReview(@RequestParam("reviewId") int reviewId, @RequestParam("userId") int userId) {
        return new ResponseEntity<>(productReviewService.likeReview(reviewId, userId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/review/new/upload")
    public ResponseEntity<ResponseMessage<String>> uploadReviewFile(@PathVariable("id") int id, @RequestParam("files") MultipartFile[] files, @RequestParam("userId") int userId) {
        ResponseMessage<String> response = new ResponseMessage<>("No files Uploaded.");
        if(files.length > 0) {
            ProductEntity productEntity = productService.getById(id);
            response = productReviewService.uploadFiles(files, id, productEntity.getStore().getUserId(), userId);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




}


