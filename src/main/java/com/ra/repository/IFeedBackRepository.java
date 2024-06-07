package com.ra.repository;

import com.ra.model.entity.FeedBack;
import com.ra.model.entity.Review;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IFeedBackRepository extends JpaRepository<FeedBack,Long> {
    @Modifying
    @Transactional
    @Query(value = "update Feedback fb set status = !fb.status where id = :id",nativeQuery = true)
    void changeFeedbackStatus(@Param("id") Long id);

    Page<FeedBack> findAllByStatusIsTrue(Pageable pageable);
}
