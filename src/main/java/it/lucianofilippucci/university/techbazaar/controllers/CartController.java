package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.helpers.CartResponse;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.CartNotFoundException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.NotAuthorizedException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ProductQuantityUnavailableException;
import it.lucianofilippucci.university.techbazaar.helpers.model.ResponseModel;
import it.lucianofilippucci.university.techbazaar.services.ProductService;
import it.lucianofilippucci.university.techbazaar.services.UnifiedAccessService;
import it.lucianofilippucci.university.techbazaar.services.mongodb.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/cart")
@RequiredArgsConstructor
public class CartController {


    private final CartService cartService;


    //private final ProductService productService;

    private final UnifiedAccessService unifiedAccessService;



    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    @GetMapping
    public ResponseEntity<ResponseModel> getUserCart(@RequestParam("cartId") String cartId) {
        CartResponse entity = cartService.getCart(cartId);
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("User Cart.")
                        .data(Map.of("cart", entity))
                        .build()
        );
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    public ResponseEntity<ResponseModel> addCartElement(
            @RequestParam("productId") int productId,
            @RequestParam("cartId") String cartId,
            @RequestParam("qty") int qty) {
        try {
            ProductEntity product = unifiedAccessService.getProduct(productId);
            if(cartService.addCartElement(product, qty, cartId))
                return ResponseEntity.ok(
                        ResponseModel.builder()
                                .timeStamp(LocalDateTime.now())
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .message("Product Added.")
                                .build()
                );
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .reason("Generic Error Occurred.")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );
        } catch(CartNotFoundException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .reason("Cart Not Found.")
                            .build()
            );
        } catch(ObjectNotFoundException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .reason("Product Not Found.")
                            .build()
            );
        }
    }



    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    @PostMapping("/update-element")
    public ResponseEntity<ResponseModel>updateCartElement(
            @RequestParam("productId") int productId,
            @RequestParam("qty") int qty,
            @RequestParam("cartId") String cartId
    ) {
        try {
            if(cartService.updateCartElement(unifiedAccessService.getProduct(productId), qty, cartId))
                return ResponseEntity.ok(
                        ResponseModel.builder()
                                .timeStamp(LocalDateTime.now())
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .message("Element Updated")
                                .build()
                );
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .reason("Generic Error Occurred.")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );
        } catch(CartNotFoundException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .reason("Cart Not Found.")
                            .build()
            );
        } catch(ObjectNotFoundException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .reason("Product Not Found.")
                            .build()
            );
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    @PostMapping("/clear")
    public ResponseEntity<ResponseModel> clearCart(@RequestParam("cartId") String cartId) {

        try {
            if(cartService.clearCart(cartId))
                return ResponseEntity.ok(
                        ResponseModel.builder()
                                .timeStamp(LocalDateTime.now())
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .message("Cart Cleared.")
                                .build()
                );

            return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .reason("Generic Error Occurred.")
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build()
            );
        } catch(CartNotFoundException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .reason("Cart Not Found.")
                            .build()
            );
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    @GetMapping("/get-coupons")
    public ResponseEntity<ResponseModel> getCartCoupons(@RequestParam("cartId") String cartId) {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Cart Coupons")
                        .data(Map.of("coupons", cartService.getCartCoupon(cartId)))
                        .build()
        );
    }

    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    @PostMapping("/place-order")
    public ResponseEntity<ResponseModel> placeOrder(@RequestParam("cartId") String cartId, @RequestParam("userAddressId") int userAddressId) {

        try {
            cartService.placeOrder(cartId, userAddressId);

        } catch (ProductQuantityUnavailableException | NotAuthorizedException | ObjectNotFoundException | CartNotFoundException e) {
            HttpStatus status = null;
            String reason = "";

            if(e instanceof ProductQuantityUnavailableException) {
                status = HttpStatus.BAD_REQUEST;
                reason = "Product Quantity Unavailable";
            } else if(e instanceof NotAuthorizedException) {
                status = HttpStatus.UNAUTHORIZED;
                reason = "You do not have Authorization.";
            } else if(e instanceof  ObjectNotFoundException) {
                status = HttpStatus.NOT_FOUND;
                reason = "One or more Product Not Found";
            } else if(e instanceof CartNotFoundException) {
                status = HttpStatus.NOT_FOUND;
                reason = "Cart not Found";
            }
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .reason(reason)
                            .status(status)
                            .statusCode(status.value())
                            .build()
            );
        }
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Order placed Successfully.")
                        .build()
        );
    }
}
