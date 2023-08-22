package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.entities.CouponEntity;
import it.lucianofilippucci.university.techbazaar.entities.ProductEntity;
import it.lucianofilippucci.university.techbazaar.entities.Role;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.CartEntity;
import it.lucianofilippucci.university.techbazaar.helpers.*;
import it.lucianofilippucci.university.techbazaar.helpers.Exceptions.ProductQuantityUnavailableException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.NotAuthorizedException;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.StoreNotFound;
import it.lucianofilippucci.university.techbazaar.helpers.model.ERole;
import it.lucianofilippucci.university.techbazaar.repositories.RoleRepository;
import it.lucianofilippucci.university.techbazaar.repositories.UserRepository;
import it.lucianofilippucci.university.techbazaar.security.JwtUtils;
import it.lucianofilippucci.university.techbazaar.services.ProductService;
import it.lucianofilippucci.university.techbazaar.services.UserDetailsImpl;
import it.lucianofilippucci.university.techbazaar.services.UserService;
import it.lucianofilippucci.university.techbazaar.services.mongodb.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")

public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);


        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item-> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles, userDetails.getCartId()));
    }

    @GetMapping("/")

    @PostMapping("/auth/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(new ResponseMessage<String>("Username already exist"));
        }
        if(userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new ResponseMessage<String>("Email already exist"));
        }

        Set<String> role = request.getRole();

        Set<Role> roles = new HashSet<>();

        UserEntity user = new UserEntity(request.getUsername(), request.getEmail(), encoder.encode(request.getPassword()));

        role.forEach(elem -> {
            switch (elem) {
                case "store":
                    Role storeRole = roleRepository.findByRoleName(ERole.ROLE_STORE).orElseThrow(() -> new RuntimeException("Error: Role not found"));
                    roles.add(storeRole);
                    break;
                case "admin":
                    Role adminRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: role not found."));
                    roles.add(adminRole);
                    break;
                default:
                    Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role not found"));
                    roles.add(userRole);
                    break;
            }
        });
        user.setRoles(roles);
        userService.newUser(user);
        cartService.newCartSession(user.getCartId());

        return ResponseEntity.ok(new ResponseMessage<String>("User Successfully Registered"));
    }


    @PostMapping("/new")
    public void newUser(@RequestBody UserEntity user) {
        UserEntity entity = userService.newUser(user);
        cartService.newCartSession(entity.getCartId());
    }

    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    @GetMapping("/cart")
    public ResponseEntity<ResponseMessage<Object>> getUserCart(@RequestParam("cartId") String cartId) {
        CartResponse entity = cartService.getCart(cartId);
        return new ResponseEntity<>(new ResponseMessage<>(entity), HttpStatus.OK);
    }




    @PostMapping("/cart/add")
    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    public ResponseEntity<ResponseMessage<Boolean>> addCartElement(
            @RequestParam("productId") int productId,
            @RequestParam("cartId") String cartId,
            @RequestParam("qty") int qty) {
        ProductEntity product = productService.getById(productId);
        return new ResponseEntity<>(new ResponseMessage<>(cartService.addCartElement(product, qty, cartId)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    @PostMapping("/cart/update-element")
    public void updateCartElement(
            @RequestParam("productId") int productId,
            @RequestParam("qty") int qty,
            @RequestParam("cartId") String cartId
    ) {
        cartService.updateCartElement(productService.getById(productId), qty, cartId);
    }

    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    @PostMapping("/cart/clear")
    public ResponseEntity<ResponseMessage<Boolean>> clearCart(@RequestParam("cartId") String cartId) {
        return new ResponseEntity<>(new ResponseMessage<>(cartService.clearCart(cartId)).setIsError(false), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/cart/get-coupons")
    public List<String> getCartCoupons(@RequestParam("cartId") String cartId) {
        return this.cartService.getCartCoupon(cartId);
    }

    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    @PostMapping("place-order")
    public ResponseEntity<ResponseMessage<String>> placeOrder(@RequestParam("cartId") String cartId, @RequestParam("userAddressId") int userAddressId) {
        ResponseMessage<String> response = new ResponseMessage<>("");
        try {
            response = cartService.placeOrder(cartId, userAddressId);
        } catch (ProductQuantityUnavailableException ex) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (NotAuthorizedException e) {
            return new ResponseEntity<>(response.setIsError(true), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(new ResponseMessage<>("Order Correctly Placed"), HttpStatus.OK);
    }


}