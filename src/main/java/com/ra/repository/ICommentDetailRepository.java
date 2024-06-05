package com.ra.repository;

import com.ra.model.entity.CommentDetail;
import com.ra.model.entity.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ICommentDetailRepository extends JpaRepository<CommentDetail,Long> {
    Page<CommentDetail> findAllByCommentId(Long id, Pageable pageable);
    @Modifying
    @Query("update CommentDetail c set c.status=case when c.status = true then false else true end where c.id=?1")
    void changeStatus(Long id);

}
