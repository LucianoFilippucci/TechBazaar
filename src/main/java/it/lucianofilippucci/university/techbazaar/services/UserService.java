package it.lucianofilippucci.university.techbazaar.services;

import it.lucianofilippucci.university.techbazaar.entities.UserAddressEntity;
import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.helpers.Helpers;
import it.lucianofilippucci.university.techbazaar.repositories.UserAddressRepository;
import it.lucianofilippucci.university.techbazaar.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAddressRepository userAddressRepository;

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
