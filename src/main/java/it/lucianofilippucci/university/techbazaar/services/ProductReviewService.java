package it.lucianofilippucci.university.techbazaar.services;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.entities.ProductReviewsEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.helpers.DropboxHelper;
import it.lucianofilippucci.university.techbazaar.helpers.DropboxResponse;
import it.lucianofilippucci.university.techbazaar.helpers.FilePathType;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.repositories.ProductReviewsRepository;
import it.lucianofilippucci.university.techbazaar.repositories.mongodb.ProductResourcesRepository;
import it.lucianofilippucci.university.techbazaar.repositories.mongodb.ReviewsLikedRepository;
import it.lucianofilippucci.university.techbazaar.services.mongodb.ProductResourcesService;
import it.lucianofilippucci.university.techbazaar.services.mongodb.ReviewsLikedService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductReviewService {

    private final ProductReviewsRepository productReviewsRepository;

    private final DropboxHelper dropboxHelper;

    private final ProductService productService;

    private final ReviewsLikedService reviewsLikedService;
    private ProductResourcesService productResourcesService;
    private final UnifiedAccessService unifiedAccessService;

    public List<ProductReviewsEntity> getAllReviews(int productId, int page, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(page, pageSize);
        Page<ProductReviewsEntity> pagedResult = productReviewsRepository.findByProductId(productId, paging);
        return pagedResult.getContent();
    }

    public boolean userReviewExists(int userId, int productId) {
        List<ProductReviewsEntity> list = productReviewsRepository.existByUserId(userId, productId);
        return !productReviewsRepository.existByUserId(userId, productId).isEmpty();
    }

    public ResponseEntity getUserReview(int reviewId) {
        Optional<ProductReviewsEntity> pre = productReviewsRepository.findProductReviewsEntitiesByReviewId(reviewId);
        return pre.map(reviewsEntity -> new ResponseEntity(reviewsEntity, HttpStatus.OK)).orElseGet(() -> new ResponseEntity(HttpStatus.BAD_REQUEST));

    }

    public boolean editReview(int reviewId, String title, String body, int stars) {
        Optional<ProductReviewsEntity> entity = productReviewsRepository.findProductReviewsEntitiesByReviewId(reviewId);
        if(entity.isPresent()) {
            ProductReviewsEntity e = entity.get();
            e.setTitle(title);
            e.setBody(body);
            e.setStarCount(stars);
            try {
                productReviewsRepository.save(e);
            } catch(DataIntegrityViolationException | InvalidDataAccessApiUsageException exception) {
                return false;
            }
        }
        return true;
    }




    public boolean newReview(int productId, int userId, int starCount, String title, String body) throws ObjectNotFoundException {
        Optional<UserEntity> user = unifiedAccessService.getUserById(userId);

        if(user.isPresent()) {
            ProductReviewsEntity productReview = new ProductReviewsEntity();
            productReview.setProduct(unifiedAccessService.getProduct(productId));
            productReview.setUser(user.get());
            productReview.setTitle(title);
            productReview.setStarCount(starCount);
            productReview.setDate(new Date());
            productReview.setBody(body);

           return productReviewsRepository.save(productReview).getReviewId() != 0;
        } else {
            throw new ObjectNotFoundException();
        }
    }

    public ResponseMessage<String> uploadFiles(MultipartFile[] files, int productId, int storeId, int userId) {
        DropboxResponse response = dropboxHelper.upload(files, FilePathType.PRODUCT_REVIEW, userId, storeId, productId);
        if(response.isError()) return new ResponseMessage<>("DropboxError -> " + response.message());
        if(this.productResourcesService.newResource(response.message(), productId, userId))
            return new ResponseMessage<>("OK").setIsError(false);
        return new ResponseMessage<>("GenericError -> ProductReviewService");
    }

    @Transactional
    public boolean likeReview(int reviewId, int userId) {
        try {
            Optional<ProductReviewsEntity> entity = productReviewsRepository.findProductReviewsEntitiesByReviewId(reviewId);

            if(entity.isPresent()) {
                ProductReviewsEntity e = entity.get();
                reviewsLikedService.likeReview(userId, e.getProduct().getProductId());
                int currLikes = e.getLikes();
                currLikes++;
                e.setLikes(currLikes);
                return true;
            }
            return false;
        } catch(Exception e) {
            return false;
        }
    }
}
