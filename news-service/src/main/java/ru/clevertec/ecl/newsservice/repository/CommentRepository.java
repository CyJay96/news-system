package ru.clevertec.ecl.newsservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.newsservice.model.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByNewsId(Long newsId, Pageable pageable);

}
