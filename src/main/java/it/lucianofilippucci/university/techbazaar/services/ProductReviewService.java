package it.lucianofilippucci.university.techbazaar.services;

import it.lucianofilippucci.university.techbazaar.entities.ProductReviewsEntity;
import it.lucianofilippucci.university.techbazaar.helpers.DropboxHelper;
import it.lucianofilippucci.university.techbazaar.helpers.FilePathType;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.repositories.ProductReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductReviewService {

    @Autowired
    private ProductReviewsRepository productReviewsRepository;

    @Autowired
    DropboxHelper dropboxHelper;

    @Autowired
    ProductService productService;

    public List<ProductReviewsEntity> getAllReviews(int productId) {
        return productReviewsRepository.findByProductId(productService.getById(productId));
    }

    public boolean newReview(ProductReviewsEntity pre) {
        ProductReviewsEntity p = productReviewsRepository.save(pre);
        return p.getProduct().getProductId() != 0;
    }

    public ResponseMessage<String> uploadFiles(MultipartFile[] files, int productId, String storeId) {
        return dropboxHelper.upload(files, productId, FilePathType.PRODUCT_REVIEW, storeId);
    }
}
