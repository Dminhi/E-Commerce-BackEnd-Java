package com.ra.repository;

import com.ra.model.entity.Review;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IReviewRepository extends JpaRepository<Review,Long> {
    @Modifying
    @Transactional
    @Query(value = "update Review rv set status = !rv.status where id = :id",nativeQuery = true)
    void changeReviewStatus(@Param("id") Long id);

    Page<Review> findAllByStatusIsTrue(Pageable pageable);

    @Query(value = "SELECT AVG(rating) AS average_rating FROM review GROUP BY productDetail_id;",nativeQuery = true)
    Long avgProductDetailRating();
}
