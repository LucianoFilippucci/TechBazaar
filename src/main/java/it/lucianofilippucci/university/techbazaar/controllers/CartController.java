package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.helpers.CartResponse;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.CartNotFoundException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.NotAuthorizedException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.services.ProductService;
import it.lucianofilippucci.university.techbazaar.services.mongodb.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    @GetMapping
    public ResponseEntity<ResponseMessage<Object>> getUserCart(@RequestParam("cartId") String cartId) {
        CartResponse entity = cartService.getCart(cartId);
        return new ResponseEntity<>(new ResponseMessage<>(entity), HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    public ResponseEntity<ResponseMessage<String>> addCartElement(
            @RequestParam("productId") int productId,
            @RequestParam("cartId") String cartId,
            @RequestParam("qty") int qty) {
        ProductEntity product = productService.getById(productId);
        try {
            if(cartService.addCartElement(product, qty, cartId))
                return new ResponseEntity<>(new ResponseMessage<>("Product Added to Cart.").setIsError(false), HttpStatus.OK);
            return new ResponseEntity<>(new ResponseMessage<>("GenericError").setIsError(true), HttpStatus.BAD_REQUEST);
        } catch(CartNotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage<>("CartNotFound").setIsError(true), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    @PostMapping("/update-element")
    public ResponseEntity<ResponseMessage<String>>updateCartElement(
            @RequestParam("productId") int productId,
            @RequestParam("qty") int qty,
            @RequestParam("cartId") String cartId
    ) {
        try {
            if(cartService.updateCartElement(productService.getById(productId), qty, cartId))
                return new ResponseEntity<>(new ResponseMessage<>("Element Updated").setIsError(false), HttpStatus.OK);
            return new ResponseEntity<>(new ResponseMessage<>("GenericError").setIsError(true), HttpStatus.OK);
        } catch(CartNotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage<>("CartNotFound").setIsError(true), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    @PostMapping("/clear")
    public ResponseEntity<ResponseMessage<String>> clearCart(@RequestParam("cartId") String cartId) {

        try {
            if(cartService.clearCart(cartId))
                return new ResponseEntity<>(new ResponseMessage<>("Cart Cleared.").setIsError(false), HttpStatus.OK);
            return new ResponseEntity<>(new ResponseMessage<>("GenericError").setIsError(true), HttpStatus.BAD_REQUEST);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage<>("CartNotFound").setIsError(true), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-coupons")
    public List<String> getCartCoupons(@RequestParam("cartId") String cartId) {
        return this.cartService.getCartCoupon(cartId);
    }

    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    @PostMapping("/place-order")
    public ResponseEntity<ResponseMessage<String>> placeOrder(@RequestParam("cartId") String cartId, @RequestParam("userAddressId") int userAddressId) {
        ResponseMessage<String> response = new ResponseMessage<>("");
        try {
            response = cartService.placeOrder(cartId, userAddressId);
        } catch (it.lucianofilippucci.university.techbazaar.helpers.Exceptions.ProductQuantityUnavailableException ex) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (NotAuthorizedException e) {
            return new ResponseEntity<>(response.setIsError(true), HttpStatus.UNAUTHORIZED);
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException(e);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(response.setIsError(true), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseMessage<>("Order Correctly Placed"), HttpStatus.OK);
    }
}
