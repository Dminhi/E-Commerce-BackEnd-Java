package com.ra.repository;

import com.ra.model.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface IReviewRepository extends JpaRepository<Comment,Long> {
    Page<Comment> findByReviewContaining(String name, Pageable pageable);
    @Modifying
    @Query("update Comment c set c.status=case when c.status = true then false else true end where c.id=?1")
    void changeStatus(Long id);
}
