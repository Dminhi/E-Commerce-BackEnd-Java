package com.ra.repository;

import com.ra.model.entity.Comment;
import com.ra.model.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ICommentRepository extends JpaRepository<Comment,Long> {
    Page<Comment> findByCommentContaining(String name, Pageable pageable);
    @Modifying
    @Query("update Comment c set c.status=case when c.status = true then false else true end where c.id=?1")
    void changeStatus(Long id);
    @Query("select c from Comment c where c.product.id = :productId")
    List<Comment> findByProductId(@Param("productId") Long productId);
}