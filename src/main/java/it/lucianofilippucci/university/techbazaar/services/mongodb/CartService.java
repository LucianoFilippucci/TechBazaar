package it.lucianofilippucci.university.techbazaar.services.mongodb;

import it.lucianofilippucci.university.techbazaar.entities.OrderEntity;
import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserAddressEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.CartEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.OrderDetailsEntity;
import it.lucianofilippucci.university.techbazaar.helpers.CartResponse;
import it.lucianofilippucci.university.techbazaar.helpers.Entities.ProductInPurchase;
import it.lucianofilippucci.university.techbazaar.helpers.Exceptions.ProductQuantityUnavailableException;
import it.lucianofilippucci.university.techbazaar.helpers.Helpers;
import it.lucianofilippucci.university.techbazaar.helpers.ProductInCart;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.repositories.OrderRepository;
import it.lucianofilippucci.university.techbazaar.repositories.UserAddressRepository;
import it.lucianofilippucci.university.techbazaar.repositories.UserRepository;
import it.lucianofilippucci.university.techbazaar.repositories.mongodb.CartRepository;
import it.lucianofilippucci.university.techbazaar.repositories.mongodb.OrderDetailsRepository;
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

    @Transactional
    public CartEntity newCartSession(String cartId) {
        CartEntity cart = new CartEntity();
        cart.setCreatedAt(new Date());
        cart.setUpdatedAt(new Date());
        cart.setCartId(cartId);
        cart.setProductsInCart(new HashMap<>());
        // think if we want to give a MaxTime we can store the cart
        return cartRepository.save(cart);
    }

    @Transactional
    public CartResponse getCart(String cartId) {
        CartEntity entity = cartRepository.findById(cartId).get();
        List<ProductInCart> cartProducts = new LinkedList<>();
        float cartTotal = 0f;
        float taxTotal = 0f;

        for(Map.Entry<Integer, Integer> entry : entity.getProductsInCart().entrySet()) {
            ProductEntity product = productService.getById(entry.getKey());
            cartTotal += product.getProductPrice() * entry.getValue();
            ProductInCart pic = new ProductInCart(entry.getKey(), product.getProductPrice(), product.getProductName(), entry.getValue());
            cartProducts.add(pic);
        }

        taxTotal = (float) (cartTotal * 0.22);
        return new CartResponse(cartTotal, taxTotal, cartProducts);
    }

    @Transactional
    public boolean deleteCart(String cartId) {
        cartRepository.deleteById(cartId);
        return true;
    }

    @Transactional
    public boolean addCartElement(ProductEntity product, int qty, String cartId) {
        Optional<CartEntity> cart = cartRepository.findById(cartId);
        if(cart.isPresent()) {
            CartEntity entity = cart.get();
            if(entity.getProductsInCart().containsKey(product.getProductId())) {
                entity.getProductsInCart().replace(product.getProductId(), qty); // replace with new qty if already present
            }
            entity.getProductsInCart().put(product.getProductId(), qty);

            cartRepository.save(entity);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateCartElement(ProductEntity product, int qty, String cartId) {
        Optional<CartEntity> cart = cartRepository.findById(cartId);
        if(cart.isPresent()) {
            CartEntity entity = cart.get();
            if(entity.getProductsInCart().containsKey(product.getProductId())) {
                if(entity.getProductsInCart().get(product.getProductId()) - qty == 0)
                    entity.getProductsInCart().remove(product.getProductId());
                else
                    entity.getProductsInCart().replace(product.getProductId(), entity.getProductsInCart().get(product.getProductId()) - qty);
                cartRepository.save(entity);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean clearCart(String cartId) {
        Optional<CartEntity> cart = cartRepository.findById(cartId);
        if(cart.isPresent()) {
            CartEntity entity = cart.get();
            entity.getProductsInCart().clear();
            cartRepository.save(entity);
            return true;
        }

        return false;
    }

    @Transactional
    public ResponseMessage<String> placeOrder(String cartId, int userAddressId) throws ProductQuantityUnavailableException {
        UserEntity user = userRepository.findUserEntityByCartId(cartId);
        CartEntity cart = cartRepository.findByCartId(cartId);
        OrderDetailsEntity orderDetails = new OrderDetailsEntity();
        float totalPrice = 0.0f;
        OrderEntity order = new OrderEntity();

        ArrayList<ProductInPurchase> productInPurchases = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : cart.getProductsInCart().entrySet()) {
            Integer productId = entry.getKey();
            Integer quantity = entry.getValue();
            ProductEntity product = productService.getById(productId);

            if (product.getProductQuantity() - quantity < 0) {
                throw new ProductQuantityUnavailableException();
            }
            float priceForProduct = product.getProductPrice() * quantity;
            productInPurchases.add(new ProductInPurchase(productId, quantity, product.getProductPrice(), priceForProduct));

            totalPrice += priceForProduct;

            product.setProductQuantity(product.getProductQuantity() - quantity);
            productService.editProduct(product);

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
