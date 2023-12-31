package it.lucianofilippucci.university.techbazaar.services.mongodb;

import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.WishListEntity;
import it.lucianofilippucci.university.techbazaar.helpers.Entities.Product;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.repositories.mongodb.WishlistRepository;
import it.lucianofilippucci.university.techbazaar.services.ProductService;
import it.lucianofilippucci.university.techbazaar.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.PagesPerMinute;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class WishlistService {

    private WishlistRepository wishlistRepository;
    private UserService userService;

    private ProductService productService;
    public WishlistService(WishlistRepository wishlistRepository, UserService userService, ProductService productService) {
        this.wishlistRepository = wishlistRepository;
        this.userService = userService;
        this.productService = productService;
    }

    public ResponseEntity<Boolean> addToWishlist(int userId, int productId) {
        try {
            Optional<UserEntity> user = this.userService.getById(userId);
            if(user.isEmpty()) return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            Optional<WishListEntity> wishlist = this.wishlistRepository.findByUserId(userId);
            Optional<ProductEntity> product = Optional.ofNullable(this.productService.getById(productId));
            if(product.isEmpty()) return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);

            if(wishlist.isEmpty()) {
                WishListEntity wish = new WishListEntity();
                wish.setUserId(userId);
                ArrayList<Integer> products = new ArrayList<>();
                products.add(productId);
                wish.setProducts(products);
                this.wishlistRepository.save(wish);
            } else {
                wishlist.get().getProducts().add(productId);
                this.wishlistRepository.save(wishlist.get());
            }

            return new ResponseEntity<>(true, HttpStatus.OK);

        } catch (ObjectNotFoundException e) {
            //TODO:
            return new ResponseEntity<>(false, HttpStatus.NOT_IMPLEMENTED);
        }
    }

    public ArrayList<Product> getWishList(int userId, int pageNumber, int pageSize) {
        Optional<WishListEntity> wishlist = this.wishlistRepository.findByUserId(userId);
        ArrayList<Product> wishList = new ArrayList<>();

        if(wishlist.isPresent()) {
            if(wishlist.get().getProducts().size() == 0) return wishList;

            for(int i = 0; i < pageSize; i++) {
                try {
                    int remainingElems = wishlist.get().getProducts().size() - (pageNumber * pageSize) - i;
                    if(remainingElems == 0)
                        break;
                    int nextElem = (pageNumber * pageSize) + i;
                    Optional<ProductEntity> product = Optional.ofNullable(this.productService.getById(wishlist.get().getProducts().get(nextElem)));

                    product.ifPresent(productEntity -> wishList.add(new Product(productEntity)));
                    //TODO: Handle if not present
                } catch (ObjectNotFoundException e) {
                    //TODO
                }
            }
        }
        return wishList;

    }

    public ResponseEntity<Boolean> remove(int userId, int productId) {
        Optional<WishListEntity> wishList = this.wishlistRepository.findByUserId(userId);
        if(wishList.isPresent()) {
            ArrayList<Integer> products = (ArrayList<Integer>) wishList.get().getProducts();
            for(int i = 0; i < products.size(); i++) {
                if(products.get(i) == productId) {
                    products.remove(i);
                    wishList.get().setProducts(products);
                    this.wishlistRepository.save(wishList.get());
                    return new ResponseEntity<>(true, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);

        } else
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Boolean> checkProductWishlist(int userId, int productId) {
        Optional<WishListEntity> wishlist = this.wishlistRepository.findByUserId(userId);
        if(wishlist.isPresent()) {
            for(Integer elem : wishlist.get().getProducts()) {
                if(elem == productId) return new ResponseEntity<>(true, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(false, HttpStatus.OK);
    }
}
