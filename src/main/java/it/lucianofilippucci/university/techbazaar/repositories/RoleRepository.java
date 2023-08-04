package it.lucianofilippucci.university.techbazaar.repositories;

import it.lucianofilippucci.university.techbazaar.entities.Role;
import it.lucianofilippucci.university.techbazaar.helpers.model.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(ERole name);
}
