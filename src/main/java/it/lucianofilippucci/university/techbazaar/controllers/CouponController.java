package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.CouponEntity;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.*;
import it.lucianofilippucci.university.techbazaar.services.CouponService;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    CouponService couponService;

    @PreAuthorize("hasRole('STORE')")
    @PostMapping("/new")
    public ResponseEntity<String> newCoupon(
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
                return new ResponseEntity<>("Coupon Created.", HttpStatus.OK);
            }
        } catch (StoreNotFound e) {
            return new ResponseEntity<>("Store Not Found.", HttpStatus.BAD_REQUEST);
        } catch (NotAuthorizedException e) {
            return new ResponseEntity<>("Not Authorized", HttpStatus.UNAUTHORIZED);
        } catch (ParseException e) {
            return new ResponseEntity<>("Date Formatting Error.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("General Error.", HttpStatus.BAD_REQUEST);

    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-details")
    public ResponseEntity<CouponEntity> getDetails(@RequestParam("code") String code) {
        CouponEntity ce = this.couponService.getCoupon(code);
        if(ce != null)
            return new ResponseEntity<>(ce, HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/use")
    public ResponseEntity<ResponseMessage<String>> useCoupon(@RequestParam("code") String code, @RequestParam("cartId") String cartId) {
        try {
            this.couponService.useCoupon(code, cartId);
            return new ResponseEntity<>(new ResponseMessage<>("Coupon Accepted").setIsError(false), HttpStatus.OK);
        } catch (CouponNotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage<>("Coupon Not Found.").setIsError(true), HttpStatus.NOT_FOUND);
        } catch (CouponMaxUseReachedException e) {
            return new ResponseEntity<>(new ResponseMessage<>("Coupon Reached Max Use.").setIsError(true), HttpStatus.BAD_REQUEST);
        } catch (CouponExpiredException e) {
            return new ResponseEntity<>(new ResponseMessage<>("Coupon Expired.").setIsError(true), HttpStatus.BAD_REQUEST);
        } catch( CouponAlreadyUsedException e) {
            return new ResponseEntity<>(new ResponseMessage<>("Coupon Already Used.").setIsError(true), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('STORE')")
    @GetMapping("/store")
    public List<CouponEntity> getStoreCoupon(@RequestParam("storeId") int storeId) {
        return this.couponService.getAllStoreCoupon(storeId);
    }
}
