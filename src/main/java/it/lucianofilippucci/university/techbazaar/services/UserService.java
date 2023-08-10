package it.lucianofilippucci.university.techbazaar.services;

import it.lucianofilippucci.university.techbazaar.entities.UserAddressEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.helpers.Helpers;
import it.lucianofilippucci.university.techbazaar.repositories.UserAddressRepository;
import it.lucianofilippucci.university.techbazaar.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAddressRepository userAddressRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsernameAndPassword(username, "@Verizon64_");
        if(user == null) {
            throw new UsernameNotFoundException("Utente non trovato: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    @Transactional
    public Optional<UserEntity> getById(int userId) {
       return this.userRepository.findUserEntityByUserId(userId);
    }

    @Transactional
    public UserEntity newUser(UserEntity user) {
        user.setCartId(Helpers.GenerateUID());
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserEntity getUserByCart(String cartId) {
        return userRepository.findUserEntityByCartId(cartId);
    }

    @Transactional(readOnly = true)
    public Optional<UserAddressEntity> getAddressByAddressId(int id) { return userAddressRepository.findById(id); }
}
