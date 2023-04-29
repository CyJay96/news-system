package ru.clevertec.ecl.newsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.clevertec.ecl.newsservice.model.entity.News;

import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {

    Optional<News> findFirstByOrderByIdAsc();

    Optional<News> findFirstByOrderByIdDesc();
}
