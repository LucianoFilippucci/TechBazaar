package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.CouponEntity;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.*;
import it.lucianofilippucci.university.techbazaar.helpers.model.ResponseModel;
import it.lucianofilippucci.university.techbazaar.services.CouponService;
import lombok.RequiredArgsConstructor;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {


    private final CouponService couponService;

    @PreAuthorize("hasRole('STORE')")
    @PostMapping("/new")
    public ResponseEntity<ResponseModel> newCoupon(
            @RequestParam("code") String code,
            @RequestParam("discount") int discount,
            @RequestParam("storeId") int storeId,
            @RequestParam("category") String category,
            @RequestParam(value = "expirationDate", required = false) String expirationDate,
            @RequestParam("maxUses") int maxUses) {

        // If maxUses = 0, it means unlimited
        // expiredDate isn't required because it's possible to have an unlimited time and/or uses.
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            if (this.couponService.newCoupon(code, discount, storeId, category, formatter.parse(expirationDate), maxUses)) {
                return ResponseEntity.ok(
                        ResponseModel.builder()
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .message("Coupon Created.")
                                .timeStamp(LocalDateTime.now())
                                .build()
                );
            } else {
                return ResponseEntity.ok(
                        ResponseModel.builder()
                                .status(HttpStatus.BAD_REQUEST)
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .reason("General Error.")
                                .timeStamp(LocalDateTime.now())
                                .build()
                );
            }
        } catch (StoreNotFound e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .reason("Store Not Found.")
                            .timeStamp(LocalDateTime.now())
                            .build()
            );
        } catch (NotAuthorizedException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .status(HttpStatus.UNAUTHORIZED)
                            .statusCode(HttpStatus.UNAUTHORIZED.value())
                            .reason("Unauthorized Access.")
                            .timeStamp(LocalDateTime.now())
                            .build()
            );
        } catch (ParseException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Date Formatting Error.")
                            .timeStamp(LocalDateTime.now())
                            .build()
            );
        }
        //

    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-details")
    public ResponseEntity<ResponseModel> getDetails(@RequestParam("code") String code) {
        CouponEntity ce = this.couponService.getCoupon(code);
        if(ce != null)
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .data(Map.of("coupon", ce))
                            .message("Coupon Details")
                            .build()
            );
        else return ResponseEntity.ok(
                ResponseModel.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .reason("Generic Error.")
                        .timeStamp(LocalDateTime.now())
                        .build()
        );
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/use")
    public ResponseEntity<ResponseModel> useCoupon(@RequestParam("code") String code, @RequestParam("cartId") String cartId) {
        try {
            this.couponService.useCoupon(code, cartId);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .message("Coupon Used.")
                            .data(Map.of("coupn", "code"))
                            .timeStamp(LocalDateTime.now())
                            .build()
            );
        } catch (CouponNotFoundException | CouponMaxUseReachedException | CouponExpiredException | CouponAlreadyUsedException e) {
            HttpStatus status = HttpStatus.OK;
            String reason = "";

            if(e instanceof CouponAlreadyUsedException) {
                status = HttpStatus.BAD_REQUEST;
                reason = "Coupon Already Used.";
            } else if( e instanceof  CouponMaxUseReachedException) {
                status = HttpStatus.BAD_REQUEST;
                reason = "Coupon Couldn't be used.";
            } else if(e instanceof CouponExpiredException) {
                status = HttpStatus.BAD_REQUEST;
                reason = "Coupon Expired";
            } else if (e instanceof CouponNotFoundException) {
                status = HttpStatus.NOT_FOUND;
                reason = "Coupon Not Found.";
            }

            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .status(status)
                            .statusCode(status.value())
                            .reason(reason)
                            .timeStamp(LocalDateTime.now())
                            .build()
            );
        }
    }

    @PreAuthorize("hasRole('STORE')")
    @GetMapping("/store")
    public ResponseEntity<ResponseModel> getStoreCoupon(@RequestParam("storeId") int storeId) {

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Store Coupons.")
                        .data(Map.of("coupons", couponService.getAllStoreCoupon(storeId)))
                        .timeStamp(LocalDateTime.now())
                        .build()
        );
    }
}
