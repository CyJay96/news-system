package ru.clevertec.ecl.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.authservice.model.entity.User;

import java.util.Optional;

/**
 * User Repository to work with the database
 *
 * @author Konstantin Voytko
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
