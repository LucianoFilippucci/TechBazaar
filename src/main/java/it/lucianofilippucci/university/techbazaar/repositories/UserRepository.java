package it.lucianofilippucci.university.techbazaar.repositories;

import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findUserEntityByCartId(String cartId);
    UserEntity findByUsernameAndPassword(String username, String password);
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<UserEntity> findUserEntityByUserId(int id);
}
