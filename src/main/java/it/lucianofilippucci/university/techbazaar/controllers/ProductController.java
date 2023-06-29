package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.helpers.Entities.Product;
import it.lucianofilippucci.university.techbazaar.helpers.Exceptions.NotAuthorizedException;
import it.lucianofilippucci.university.techbazaar.helpers.Exceptions.ProductIdNotFound;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.TelegramSender;
import it.lucianofilippucci.university.techbazaar.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    TelegramSender telegramSender;

    @GetMapping
    public List<Product> getAll() throws ProductIdNotFound {
        List<ProductEntity> products = productService.getAllProducts();
        ArrayList<Product> result = new ArrayList<Product>();
        if(products.size() > 0) {
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

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getProduct(@PathVariable("id") int id) {
        try{
            Product product = new Product(productService.getById(id));
            return new ResponseEntity<>(new ResponseMessage<>(product).setIsError(false), HttpStatus.OK);
        } catch (ProductIdNotFound pidnf) {
            return new ResponseEntity<>(new ResponseMessage<>("The product with id [" + id + "] couldn't be found").setIsError(true), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/edit")
    public ResponseEntity<ResponseMessage> editProduct(@RequestBody Product product) {
        ResponseMessage<String> responseMessage = new ResponseMessage<>("");
        try{
            responseMessage = productService.editProduct(product);
            return new ResponseEntity<>(responseMessage.setIsError(false), HttpStatus.OK);
        } catch (DataAccessException dae) {
            telegramSender.sendMessageToUser("ERROR FROM CLASS " + this.getClass().getName() + " ON editProduct()\n WITH EXCEPTION DataAccessException");
            telegramSender.sendMessageToUser(dae.getMessage());
            telegramSender.sendMessageToUser(responseMessage.getMessage());
            return new ResponseEntity<>(responseMessage.setIsError(true), HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<ResponseMessage> newProduct(@RequestBody Product product) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductPrice(product.getPrice());
        productEntity.setProductName(product.getName());
        productEntity.setProductQuantity(product.getQty());
        productEntity.setStoreIdentifier(product.getStoreId());
        productEntity.setCategory(product.getCategory());


        productEntity.setProductDescription(product.getDescription());

        try {
            productService.newProduct(productEntity);
        } catch(DataAccessException dae) {
            telegramSender.sendMessageToUser("ERROR FROM CLASS " + this.getClass().getName() + "ON newProduct()\n WITH EXCEPTION DataAccessException");
            telegramSender.sendMessageToUser(dae.getMessage());
        }
        return new ResponseEntity<>(new ResponseMessage("yay").setIsError(false), HttpStatus.OK);
    }
}
