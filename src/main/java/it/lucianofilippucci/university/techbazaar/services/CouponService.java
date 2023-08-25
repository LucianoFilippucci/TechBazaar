package it.lucianofilippucci.university.techbazaar.services;


import it.lucianofilippucci.university.techbazaar.controllers.CartController;
import it.lucianofilippucci.university.techbazaar.entities.CouponEntity;
import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.CartEntity;
import it.lucianofilippucci.university.techbazaar.helpers.CartResponse;
import it.lucianofilippucci.university.techbazaar.helpers.ProductInCart;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.*;
import it.lucianofilippucci.university.techbazaar.helpers.model.CartCouponModel;
import it.lucianofilippucci.university.techbazaar.helpers.model.ProductInCartModel;
import it.lucianofilippucci.university.techbazaar.repositories.CouponRepository;
import it.lucianofilippucci.university.techbazaar.repositories.UserRepository;
import it.lucianofilippucci.university.techbazaar.repositories.mongodb.CartRepository;
import it.lucianofilippucci.university.techbazaar.services.mongodb.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CouponService {
    @Autowired
    CouponRepository couponRepository;

    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductService productService;

    public List<CouponEntity> getAvailableCoupon() {
        return couponRepository.findAll();
    }

    public List<CouponEntity> getAllStoreCoupon(int storeId) {
        return couponRepository.findAllByStore(storeId);
    }

    public boolean newCoupon(String code, int discount, int storeId, String category, Date expirationDate, int maxUses) throws StoreNotFound, NotAuthorizedException {
        CouponEntity ce = new CouponEntity();
        Optional<UserEntity> ue = userService.getById(storeId);
        if(ue.isPresent()) {
            if(ue.get().getUserId() == storeId) {
                ce.setDiscount(discount);
                ce.setCode(code);
                ce.setStore(ue.get());
                ce.setCategory(category);
                ce.setExpirationDate(expirationDate);
                ce.setMaxUses(maxUses);

                couponRepository.save(ce);
                return true;
            } else {
                throw new NotAuthorizedException();
            }
        } else {
            throw new StoreNotFound();
        }
    }

    public CouponEntity getCoupon(String code) {
        Optional<CouponEntity> ce = this.couponRepository.findById(code);
        return ce.orElse(null);
    }

    @Transactional
    public void useCoupon(String coupon, String cartId) throws CouponNotFoundException, CouponMaxUseReachedException, CouponExpiredException, CouponAlreadyUsedException {
        CartResponse userCart = this.cartService.getCart(cartId);
        UserEntity user = this.userService.getUserByCart(cartId);
        CouponEntity couponEntity = getCoupon(coupon);
        CartEntity ce = this.cartRepository.findByCartId(cartId);


        List<CartCouponModel> cartCoupons = ce.getCoupons();
        boolean isPresent = false;

        if(couponEntity != null) {
            UserEntity store = couponEntity.getStore();
            for(CartCouponModel ccm : cartCoupons) {
                if(ccm.getCouponCode().equals(couponEntity.getCode())) {
                    isPresent = true;
                    break;
                }
            }
            if(!isPresent) {
                for(ProductInCartModel picm : ce.getProductsInCart()) {
                    ProductEntity productEntity = productService.getById(picm.getProductId());
                    if(productEntity.getProductCategory().equals(couponEntity.getCategory())) {
                        Date now = new Date();
                        Timestamp timestamp = new Timestamp(now.getTime());
                        Date exp = couponEntity.getExpirationDate();
                        if(timestamp.before(exp)) {
                            if(couponEntity.getTimesUsed() >= couponEntity.getMaxUses()) throw new CouponMaxUseReachedException();
                            float discount = (picm.getProductPrice() * couponEntity.getDiscount()) / 100;
                            float newPrice = picm.getProductPrice() - discount;
                            picm.setProductPrice(newPrice);
                            couponEntity.setTimesUsed(couponEntity.getTimesUsed() + 1);
                            ce.getCoupons().add(new CartCouponModel(couponEntity.getCode(), couponEntity.getDiscount()));
                            user.getUsedCoupon().add(couponEntity);
                        } else throw new CouponExpiredException();
                    }
                }

            } else throw new CouponAlreadyUsedException();
            this.userRepository.save(user);
            this.userRepository.save(store);
            this.cartRepository.save(ce);
        } throw new CouponNotFoundException();




    }

}
