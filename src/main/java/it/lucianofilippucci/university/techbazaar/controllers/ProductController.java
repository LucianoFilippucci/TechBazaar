package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.helpers.Entities.Product;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.NotAuthorizedException;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.TelegramSender;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.StoreNotFound;
import it.lucianofilippucci.university.techbazaar.services.ProductReviewService;
import it.lucianofilippucci.university.techbazaar.services.ProductService;

import it.lucianofilippucci.university.techbazaar.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    UserService userService;


    @GetMapping
    public List<Product> getAll() throws ObjectNotFoundException {
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
    public List<Product> getBySearchbox(@PathVariable("keyword") String keyword) throws ObjectNotFoundException {
        return productService.getContainingKeywords(keyword);
    }

    @GetMapping("/single-item")
    public ResponseEntity<ResponseMessage> getProduct(@RequestParam("productId") int id) {

        Product product = new Product(productService.getById(id));
        return new ResponseEntity<>(new ResponseMessage<>(product).setIsError(false), HttpStatus.OK);

    }

    @PostMapping("/edit")
    public ResponseEntity<ResponseMessage<String>> editProduct(@RequestParam("productId") int productId, @RequestParam("storeId") int storeId, @RequestParam(value = "price", required = false) float price, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "description", required = false) String description, @RequestParam(value = "category", required = false) String category, @RequestParam(value = "qty") int qty) {
        ResponseMessage<String> responseMessage = new ResponseMessage<>("");
        try{
            responseMessage = productService.editProduct(productId, storeId, price, name, description, category, qty);
            return new ResponseEntity<>(responseMessage.setIsError(false), HttpStatus.OK);
        } catch (DataAccessException dae) {
            return new ResponseEntity<ResponseMessage<String>>(responseMessage.setIsError(true), HttpStatus.BAD_REQUEST);
        } catch(NotAuthorizedException exception) {
            return new ResponseEntity<>(responseMessage, HttpStatus.UNAUTHORIZED);
        }

    }

    @PreAuthorize("hasRole('STORE')")
    @PostMapping("/delete")
    public ResponseEntity<ResponseMessage> deleteProduct(@RequestParam("productId") int productId, @RequestParam int storeId) {
        ResponseMessage responseMessage = new ResponseMessage<>("");
        try {
            responseMessage = productService.deleteProduct(productId,storeId);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch(NotAuthorizedException nae) {
            return new ResponseEntity<>(responseMessage, HttpStatus.UNAUTHORIZED);
        }

    }

    @PreAuthorize("hasRole('STORE')")
    @PostMapping("/new")
    public ResponseEntity<ProductEntity> newProduct(
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
        productEntity.setStore(userService.getById(storeId).get());
        productEntity.setProductCategory(category);
        productEntity.setProductDescription(description);

        try {
            ProductEntity pe = productService.newProduct(productEntity);
//            if(files != null && files.length > 0)
//                return new ResponseEntity<>(productService.uploadFiles(files, pe.getProductId(), pe.getStore().getUserId()), HttpStatus.OK);
            return new ResponseEntity<>(pe, HttpStatus.OK);
        } catch(DataAccessException dae) {
            //telegramSender.sendMessageToUser("ERROR FROM CLASS " + this.getClass().getName() + "ON newProduct()\n WITH EXCEPTION DataAccessException");
            //telegramSender.sendMessageToUser(dae.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/store-products")
    public ResponseEntity<List<ProductEntity>> getStoreProducts(@RequestParam("storeId") int storeId) {
        try {
            return this.userService.getStoreProducts(storeId);
        } catch (StoreNotFound e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }

}
