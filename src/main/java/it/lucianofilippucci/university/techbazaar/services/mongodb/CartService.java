package it.lucianofilippucci.university.techbazaar.services.mongodb;

import it.lucianofilippucci.university.techbazaar.entities.*;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.CartEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.OrderDetailsEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.StoreOrderEntity;
import it.lucianofilippucci.university.techbazaar.helpers.CartResponse;
import it.lucianofilippucci.university.techbazaar.helpers.Entities.ProductInPurchase;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ProductQuantityUnavailableException;
import it.lucianofilippucci.university.techbazaar.helpers.Helpers;
import it.lucianofilippucci.university.techbazaar.helpers.ProductInCart;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.CartNotFoundException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.NotAuthorizedException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.helpers.model.ProductInCartModel;
import it.lucianofilippucci.university.techbazaar.repositories.CouponRepository;
import it.lucianofilippucci.university.techbazaar.repositories.OrderRepository;
import it.lucianofilippucci.university.techbazaar.repositories.UserAddressRepository;
import it.lucianofilippucci.university.techbazaar.repositories.UserRepository;
import it.lucianofilippucci.university.techbazaar.repositories.mongodb.CartRepository;
import it.lucianofilippucci.university.techbazaar.repositories.mongodb.OrderDetailsRepository;
import it.lucianofilippucci.university.techbazaar.repositories.mongodb.StoreOrderRepository;
import it.lucianofilippucci.university.techbazaar.services.ProductService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserAddressRepository userAddressRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    ProductService productService;

    @Autowired
    StoreOrderRepository storeOrderRepository;

    @Autowired
    CouponRepository couponRepository;


    @Transactional
    public void newCartSession(String cartId) {
        CartEntity cart = new CartEntity();
        cart.setCreatedAt(new Date());
        cart.setUpdatedAt(new Date());
        cart.setCartId(cartId);
        cart.setProductsInCart(new ArrayList<>());
        cart.setCoupons(new ArrayList<>());
        //TODO: think if we want to give a MaxTime we can store the cart
        cartRepository.save(cart);
    }

    @Transactional
    public CartResponse getCart(String cartId) {
        CartEntity entity = cartRepository.findById(cartId).get();
        List<ProductInCart> cartProducts = new LinkedList<>();
        float cartTotal = 0f;
        float taxTotal = 0f;
        float totalToPay = 0f;
        List<CouponEntity> validCoupons = new ArrayList<>();

        for(int i = 0; i < entity.getProductsInCart().size(); i++) {
            ProductInCartModel picm = entity.getProductsInCart().get(i);
            ProductEntity product = productService.getById(picm.getProductId());
            ProductInCart pic = new ProductInCart(product.getProductId(), product.getProductPrice(), product.getProductName(), picm.getQty(), product.getProductCategory());
            cartProducts.add(pic);

            cartTotal += product.getProductPrice() * picm.getQty();
            totalToPay += (product.getProductPrice() * picm.getQty()) - (picm.getProductPrice() * picm.getQty());
            taxTotal =  (cartTotal * product.getIva()) / 100;

        }

        taxTotal = (float) (cartTotal * 0.22);
        return new CartResponse(cartTotal, taxTotal, totalToPay, validCoupons, cartProducts);
    }

    public List<String> getCartCoupon(String cartId) {
        CartEntity entity = this.cartRepository.findByCartId(cartId);
        List<String> coupons = new ArrayList<>();

        if(entity.getCoupons() != null) {
            for(int i = 0; i < entity.getCoupons().size(); i++) {
                Optional<CouponEntity> coupon = this.couponRepository.findById(entity.getCoupons().get(i).getCouponCode());
                coupon.ifPresent(couponEntity -> coupons.add(couponEntity.getCode()));
            }
        }

        return coupons;
    }

    @Transactional
    public boolean deleteCart(String cartId) {
        cartRepository.deleteById(cartId);
        return true;
    }

    @Transactional
    public boolean addCartElement(ProductEntity product, int qty, String cartId) throws CartNotFoundException {
        Optional<CartEntity> cart = cartRepository.findById(cartId);
        boolean found = false;

        if(cart.isPresent()) {
            CartEntity cartEntity = cart.get();
            for(int i = 0; i < cartEntity.getProductsInCart().size(); i++) {
                ProductInCartModel picm = cartEntity.getProductsInCart().get(i);
                if(picm.getProductId() == product.getProductId()) {
                    found = true;
                    cartEntity.getProductsInCart().get(i).setQty(picm.getQty() + qty);
                }
            }
            if(!found) {
                cartEntity.getProductsInCart().add(new ProductInCartModel(product.getProductId(), qty, product.getProductPrice()));
            }

            cartRepository.save(cartEntity);
            return true;
        } else throw new CartNotFoundException();
    }

    @Transactional
    public boolean updateCartElement(ProductEntity product, int qty, String cartId) throws  CartNotFoundException{
        Optional<CartEntity> cart = cartRepository.findById(cartId);
        if(cart.isPresent()) {
            CartEntity entity = cart.get();

            for(int i = 0; i < entity.getProductsInCart().size(); i++) {
                ProductInCartModel picm = entity.getProductsInCart().get(i);

                if(product.getProductId() == picm.getProductId()) {
                    if(picm.getQty() + qty == 0)
                        entity.getProductsInCart().remove(picm);
                    else
                        entity.getProductsInCart().get(i).setQty(picm.getQty() + qty);
                    cartRepository.save(entity);
                    return true;
                }
            }
            return false;
        } else
            throw new CartNotFoundException();
    }

    @Transactional
    public boolean clearCart(String cartId) throws CartNotFoundException {
        Optional<CartEntity> cart = cartRepository.findById(cartId);
        if(cart.isPresent()) {
            CartEntity entity = cart.get();
            entity.getProductsInCart().clear();
            entity.getCoupons().clear();
            cartRepository.save(entity);
            return true;
        } else throw new CartNotFoundException();
    }

    @Transactional
    public ResponseMessage<String> placeOrder(String cartId, int userAddressId) throws ProductQuantityUnavailableException, NotAuthorizedException, ObjectNotFoundException, CartNotFoundException {
        // TODO: understand how to explicit rollback, atm it's unsage

        UserEntity user = userRepository.findUserEntityByCartId(cartId);
        CartEntity cart = cartRepository.findByCartId(cartId);
        OrderDetailsEntity orderDetails = new OrderDetailsEntity();
        float totalPrice = 0.0f;
        OrderEntity order = new OrderEntity();

        // Integer = storeId
        HashMap<Integer, List<ProductInPurchase>> storesProducts = new HashMap<>();

        ArrayList<ProductInPurchase> productInPurchases = new ArrayList<>();

        for(int i = 0; i < cart.getProductsInCart().size(); i++) {
            ProductInCartModel picm = cart.getProductsInCart().get(i);
            Optional<ProductEntity> product1 = Optional.ofNullable(productService.getById(picm.getProductId()));

            if(product1.isPresent()) {
                ProductEntity product = product1.get();
                // TODO: Think if we should pass the info about the max qty available when throwing the error.
                if(product.getProductQuantity() - picm.getQty() < 0) throw new ProductQuantityUnavailableException();
                float priceForProduct = picm.getProductPrice() * picm.getQty();
                ProductInPurchase pip = new ProductInPurchase(product.getProductName(), picm.getProductId(), picm.getQty(), picm.getProductPrice(), priceForProduct);
                productInPurchases.add(pip);

                totalPrice += priceForProduct;

                product.setProductQuantity(product.getProductQuantity() - picm.getQty());
                product.setProductTotalSelt(product.getProductTotalSelt() + picm.getQty());

                if(storesProducts.containsKey(product.getStore().getUserId()))
                    storesProducts.get(product.getStore().getUserId()).add(pip);
                else {
                    List<ProductInPurchase> temp = new ArrayList<>();
                    temp.add(pip);
                    storesProducts.put(product.getStore().getUserId(), temp);
                }
                productService.editProductQtyAfterSelling(product.getProductId(), product.getProductQuantity(), picm.getQty());
            } else throw new ObjectNotFoundException();


        }

        orderDetails.setOrderDetailIdentifier(Helpers.GenerateUID());
        orderDetails.setTotal(totalPrice);
        orderDetails.setProductInPurchases(productInPurchases);

        order.setOrderId(Helpers.GenerateUID());

        orderDetails.setOrderId(order.getOrderId());
        order.setOrderTotal(totalPrice);
        order.setDate(new Date());
        UserAddressEntity userAddress = userAddressRepository.findById(userAddressId).get();
        order.setShippingAddr(userAddress.getVia() + ", " + userAddress.getCivico() + ", " + userAddress.getProvincia());
        order.setUser(user);
        order.setContactInfo("");

        for (Map.Entry<Integer, List<ProductInPurchase>> entry : storesProducts.entrySet()) {
            StoreOrderEntity storeOrderEntity = new StoreOrderEntity();
            storeOrderEntity.setUserOrderId(order.getOrderId());
            storeOrderEntity.setOrderId(Helpers.GenerateUID());
            storeOrderEntity.setProductInPurchases(entry.getValue());
            storeOrderEntity.setStoreId(entry.getKey());
            storeOrderEntity.setOrderDate(new Date());
            Collection<UserAddressEntity> address = user.getUserAddressEntities();
            address.forEach(entity -> {
                if(entity.getIsDefault() == 1)
                    storeOrderEntity.setUserAddress(entity.toString());
            });
            float totalOrder = 0F;
            for(ProductInPurchase pip : entry.getValue()) {
                totalOrder += pip.getTotalProductPrice();
            }
            storeOrderEntity.setTotal(totalOrder);
            storeOrderRepository.save(storeOrderEntity);
        }


        clearCart(cartId);

        Collection<OrderEntity> orderEntities = user.getOrderEntities();
        orderEntities.add(order);
        user.setOrderEntities(orderEntities);
        order.setOrderStatus(OrderEntity.OrderStatus.PENDING);

        orderRepository.save(order);
        userRepository.save(user);
        orderDetailsRepository.save(orderDetails);

        return new ResponseMessage<>("Order Correctly saved").setIsError(false);
    }

}
