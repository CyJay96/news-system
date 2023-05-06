package ru.clevertec.ecl.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.authservice.model.entity.Role;

import java.util.Optional;

/**
 * Role Repository to work with the database
 *
 * @author Konstantin Voytko
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
