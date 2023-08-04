package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.helpers.Entities.Product;
import it.lucianofilippucci.university.techbazaar.helpers.Exceptions.NotAuthorizedException;
import it.lucianofilippucci.university.techbazaar.helpers.Exceptions.ProductIdNotFound;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.TelegramSender;
import it.lucianofilippucci.university.techbazaar.services.ProductReviewService;
import it.lucianofilippucci.university.techbazaar.services.ProductService;
import it.lucianofilippucci.university.techbazaar.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    TelegramSender telegramSender;

    @Autowired
    ProductReviewService productReviewService;

    @Autowired
    StoreService storeService;

    @GetMapping
    public List<Product> getAll() throws ProductIdNotFound {
        List<ProductEntity> products = productService.getAllProducts();
        ArrayList<Product> result = new ArrayList<Product>();
        if(!products.isEmpty()) {
           for(ProductEntity pe : products) {
               result.add(new Product(pe));
           }
        }
        return result;
    }

    @GetMapping("/search/{keyword}")
    public List<Product> getBySearchbox(@PathVariable("keyword") String keyword) throws ProductIdNotFound {
        return productService.getContainingKeywords(keyword);
    }

    @GetMapping("/single-item")
    public ResponseEntity<ResponseMessage> getProduct(@RequestParam("productId") int id) {

        Product product = new Product(productService.getById(id));
        return new ResponseEntity<>(new ResponseMessage<>(product).setIsError(false), HttpStatus.OK);

    }

    @PostMapping("/{id}/edit")
    public ResponseEntity<ResponseMessage<String>> editProduct(@RequestBody ProductEntity product) {
        ResponseMessage<String> responseMessage = new ResponseMessage<>("");
        try{
            responseMessage = productService.editProduct(product);
            return new ResponseEntity<>(responseMessage.setIsError(false), HttpStatus.OK);
        } catch (DataAccessException dae) {
            telegramSender.sendMessageToUser("ERROR FROM CLASS " + this.getClass().getName() + " ON editProduct()\n WITH EXCEPTION DataAccessException");
            telegramSender.sendMessageToUser(dae.getMessage());
            telegramSender.sendMessageToUser(responseMessage.getMessage());
            return new ResponseEntity<ResponseMessage<String>>(responseMessage.setIsError(true), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<ResponseMessage> deleteProduct(@PathVariable("id") int id, @RequestBody String storeId) {
        ResponseMessage responseMessage = new ResponseMessage<>("");
        try {
            responseMessage = productService.deleteProduct(id,storeId);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch(NotAuthorizedException nae) {
            return new ResponseEntity<>(responseMessage, HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/new")
    public ResponseEntity<ResponseMessage<String>> newProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("price") float price,
            @RequestParam("qty") int qty,
            @RequestParam("storeId") String storeId,
            @RequestParam(value = "files", required = false) MultipartFile[] files
    ) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductPrice(price);
        productEntity.setProductName(name);
        productEntity.setProductQuantity(qty);
        productEntity.setStore(storeService.getStoreById(storeId));
        productEntity.setProductCategory(category);
        productEntity.setProductDescription(description);

        try {
            ProductEntity pe = productService.newProduct(productEntity);
            if(files != null && files.length > 0)
                return new ResponseEntity<>(productService.uploadFiles(files, pe.getProductId(), pe.getStore().getStoreId()), HttpStatus.OK);
            return new ResponseEntity<>(new ResponseMessage<>("Product Correctly saved Without Any Image.").setIsError(false), HttpStatus.OK);
        } catch(DataAccessException dae) {
            telegramSender.sendMessageToUser("ERROR FROM CLASS " + this.getClass().getName() + "ON newProduct()\n WITH EXCEPTION DataAccessException");
            telegramSender.sendMessageToUser(dae.getMessage());
            return new ResponseEntity<>(new ResponseMessage<>("WOWOWOWO PROBLEMI").setIsError(true), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/review/new")
    public ResponseEntity<ResponseMessage<String>> fileUpload(@PathVariable("id") int id, @RequestParam("files")MultipartFile[] files) {
        if(files.length > 0) {
            ProductEntity productEntity = productService.getById(id);
            return new ResponseEntity<>(productReviewService.uploadFiles(files, id, productEntity.getStore().getStoreId()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseMessage<>("No files received."), HttpStatus.OK);
    }



    @PostMapping("/{id}/review/new/upload")
    public ResponseEntity<ResponseMessage<String>> uploadReviewFile(@PathVariable("id") int id, @RequestParam("files") MultipartFile[] files) {
        ResponseMessage<String> response = new ResponseMessage<>("No files Uploaded.");
        if(files.length > 0) {
            ProductEntity productEntity = productService.getById(id);
            response = productReviewService.uploadFiles(files, id, productEntity.getStore().getStoreId());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
