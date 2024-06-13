package com.ra.repository;

import com.ra.model.entity.Comment;

import com.ra.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ICommentRepository extends JpaRepository<Comment,Long> {
    Page<Comment> findByCommentContaining(String name, Pageable pageable);
    @Modifying
    @Query(value = "update Comment c set c.commentStatus = not c.commentStatus where c.id = :id",nativeQuery = true)
    void changeStatus(Long id);
    @Query("select c from Comment c where c.productDetail.id = :productDetailId and (c.user.id=:userId or c.commentStatus = true)")
    Page<Comment> findAllByProductDetailId(Long productDetailId,Long userId,Pageable pageable);

    Page<Comment> findAllByProductDetailIdAndCommentStatusIsTrue(Long productDetailId, Pageable pageable);

}
