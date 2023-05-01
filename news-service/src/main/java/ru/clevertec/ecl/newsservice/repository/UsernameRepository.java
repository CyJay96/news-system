package ru.clevertec.ecl.newsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.newsservice.model.entity.Username;

import java.util.Optional;

public interface UsernameRepository extends JpaRepository<Username, Long> {

    Optional<Username> findFirstByOrderByIdDesc();
}
