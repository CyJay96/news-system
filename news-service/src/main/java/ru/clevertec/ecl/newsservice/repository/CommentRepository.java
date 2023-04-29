package ru.clevertec.ecl.newsservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.clevertec.ecl.newsservice.model.entity.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    Page<Comment> findAllByNewsId(Long newsId, Pageable pageable);

    Optional<Comment> findFirstByOrderByIdAsc();

    Optional<Comment> findFirstByOrderByIdDesc();
}
