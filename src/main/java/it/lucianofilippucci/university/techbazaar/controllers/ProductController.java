package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.NotAuthorizedException;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.TelegramSender;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.UserNotAStoreException;
import it.lucianofilippucci.university.techbazaar.helpers.model.ResponseModel;
import it.lucianofilippucci.university.techbazaar.services.UnifiedAccessService;
import it.lucianofilippucci.university.techbazaar.services.ProductReviewService;
import it.lucianofilippucci.university.techbazaar.services.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/product")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    TelegramSender telegramSender;

    @Autowired
    ProductReviewService productReviewService;

    private final UnifiedAccessService unifiedAccessService;


    @GetMapping
    public ResponseEntity<ResponseModel> getAll() throws ObjectNotFoundException {
        //TODO: Why the fuck i'm throwing there an exception?
        Collection<ProductEntity> products = productService.getAllProducts();

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("products")
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("products", products))
                        .build()
        );
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<ResponseModel> getBySearchbox(@PathVariable("keyword") String keyword) throws ObjectNotFoundException {
        //return productService.getContainingKeywords(keyword);
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(HttpStatus.NOT_IMPLEMENTED)
                        .message("Functionality not implemented yet.")
                        .reason("Functionality not implemented.")
                        .statusCode(HttpStatus.NOT_IMPLEMENTED.value())
                        .build()
        );
    }

    @GetMapping("/single-item")
    public ResponseEntity<ResponseModel> getProduct(@RequestParam("productId") int id) {
        ProductEntity product = new ProductEntity();
        HttpStatus status;
        try {
            product = productService.getById(id);
            status = HttpStatus.OK;
        } catch (ObjectNotFoundException e) {
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(status.value())
                        .status(status)
                        .reason(status == HttpStatus.NOT_FOUND ? "Product ID not found." : "")
                        .data(status == HttpStatus.OK ? Map.of("product", product) : null)
                        .build()
        );

    }

    @PostMapping("/edit")
    public ResponseEntity<ResponseModel> editProduct(
            @RequestParam("productId") int productId,
            @RequestParam("storeId") int storeId,
            @RequestParam(value = "price", required = false) float price,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "qty") int qty) {


        try {
            ProductEntity product;
            if((product = productService.editProduct(productId, storeId, price, name, description, category, qty)) != null) {
                return ResponseEntity.ok(
                        ResponseModel.builder()
                                .timeStamp(LocalDateTime.now())
                                .message("Product Updated.")
                                .statusCode(HttpStatus.OK.value())
                                .status(HttpStatus.OK)
                                .data(Map.of("product", product))
                                .build()
                );
            }
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Generic Error Occurred.")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );
        } catch (NotAuthorizedException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Unauthorized Access.")
                            .reason("Unauthorized Access.")
                            .statusCode(HttpStatus.UNAUTHORIZED.value())
                            .status(HttpStatus.UNAUTHORIZED)
                            .build()
            );
        }
    }

    @PreAuthorize("hasRole('STORE')")
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseModel> deleteProduct(@RequestParam("productId") int productId, @RequestParam int storeId) {

        try {
            if(productService.deleteProduct(productId, storeId))
                return ResponseEntity.ok(
                        ResponseModel.builder()
                                .statusCode(HttpStatus.OK.value())
                                .status(HttpStatus.OK)
                                .message("Product Deleted.")
                                .timeStamp(LocalDateTime.now())
                                .build()
                );

            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Generic Error Occurred.")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );
        } catch (NotAuthorizedException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Unauthorized Access.")
                            .reason("Unauthorized Access.")
                            .statusCode(HttpStatus.UNAUTHORIZED.value())
                            .status(HttpStatus.UNAUTHORIZED)
                            .build()
            );
        }
    }

    @PreAuthorize("hasRole('STORE')")
    @PostMapping("/new")
    public ResponseEntity<ResponseModel> newProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("price") float price,
            @RequestParam("qty") int qty,
            @RequestParam("storeId") int storeId,
            @RequestParam(value = "files", required = false) MultipartFile[] files
    ) {




        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductPrice(price);
        productEntity.setProductName(name);
        productEntity.setProductQuantity(qty);
        productEntity.setProductCategory(category);
        productEntity.setProductDescription(description);

        try {
            productEntity.setStore(unifiedAccessService.getStoreById(storeId));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .reason("Store Not Found.")
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .build()
            );
        } catch(UserNotAStoreException ex) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .reason("Requesting User Not A Store.")
                            .status(HttpStatus.FORBIDDEN)
                            .statusCode(HttpStatus.FORBIDDEN.value())
                            .build()
            );
        }
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED)
                        .message("Product Created.")
                        .data(Map.of("product", productService.newProduct(productEntity)))
                        .build()
        );
//            if(files != null && files.length > 0)
//                return new ResponseEntity<>(productService.uploadFiles(files, pe.getProductId(), pe.getStore().getUserId()), HttpStatus.OK);
    }

    @GetMapping("/store-products")
    public ResponseEntity<ResponseModel> getStoreProducts(@RequestParam("storeId") int storeId) {
        try {
            UserEntity store = unifiedAccessService.getStoreById(storeId);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Store Products")
                            .statusCode(HttpStatus.FOUND.value())
                            .status(HttpStatus.FOUND)
                            .data(Map.of("products", productService.getByStoreId(store)))
                            .build()
            );
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .reason("Store Not Found.")
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .build()
            );
        } catch(UserNotAStoreException ex) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .reason("Requesting User Not A Store.")
                            .status(HttpStatus.FORBIDDEN)
                            .statusCode(HttpStatus.FORBIDDEN.value())
                            .build()
            );
        }

    }

    @PreAuthorize("hasRole('STORE')")
    @PostMapping("/upload-images")
    public ResponseEntity<ResponseMessage<String>> uploadImages(@RequestParam("files") MultipartFile[] file, @RequestParam("productId") int productId, @RequestParam("storeId") int storeId) {
        return new ResponseEntity<>(this.productService.uploadFiles(file, productId, storeId), HttpStatus.OK);
    }

}
