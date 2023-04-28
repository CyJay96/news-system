package ru.clevertec.ecl.newsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.newsservice.model.entity.News;

public interface NewsRepository extends JpaRepository<News, Long> {
}
