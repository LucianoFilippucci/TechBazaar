package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.CartEntity;
import it.lucianofilippucci.university.techbazaar.helpers.Exceptions.ProductQuantityUnavailableException;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.services.ProductService;
import it.lucianofilippucci.university.techbazaar.services.UserService;
import it.lucianofilippucci.university.techbazaar.services.mongodb.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    @PostMapping("/new")
    public void newUser(@RequestBody UserEntity user) {
        UserEntity entity = userService.newUser(user);
        cartService.newCartSession(entity.getCartId());
    }

    @GetMapping("/cart")
    public ResponseEntity<ResponseMessage<Object>> getUserCart(@RequestParam("cartId") String cartId) {
        CartEntity entity = cartService.getCart(cartId);
        return new ResponseEntity<>(new ResponseMessage<>(entity), HttpStatus.OK);
    }



    @PostMapping("/cart/add")
    public ResponseEntity<ResponseMessage<Boolean>> addCartElement(
            @RequestParam("productId") int productId,
            @RequestParam("cartId") String cartId,
            @RequestParam("qty") int qty) {
        ProductEntity product = productService.getById(productId);
        return new ResponseEntity<>(new ResponseMessage<>(cartService.addCartElement(product, qty, cartId)), HttpStatus.OK);
    }

    @PostMapping("/cart/update-element")
    public void updateCartElement(
            @RequestParam("productId") int productId,
            @RequestParam("qty") int qty,
            @RequestParam("cartId") String cartId
    ) {
        cartService.updateCartElement(productService.getById(productId), qty, cartId);
    }

    @PostMapping("place-order")
    public ResponseEntity<ResponseMessage<String>> placeOrder(@RequestParam("cartId") String cartId, @RequestParam("userAddressId") int userAddressId) {
        try {
            cartService.placeOrder(cartId, userAddressId);
        } catch (ProductQuantityUnavailableException ex) {
            return new ResponseEntity<>(new ResponseMessage<>("one or more product quantity not available"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseMessage<>("Order Correctly Placed"), HttpStatus.OK);
    }


}
