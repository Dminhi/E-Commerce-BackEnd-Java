package com.ra.repository;

import com.ra.model.entity.WishList;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface IWishListRepository extends JpaRepository<WishList,Long> {
    boolean existsByProductIdAndUserId(Long productId,Long userId);

    @Transactional
    void deleteWishListByProductIdAndUserId(Long productId,Long userId);

    @Transactional
    void deleteWishListById(Long id);

    Page<WishList> findAllByUserId(Long userId, Pageable pageable);

    @Query("select wl.product.id from WishList wl where wl.user.id = :id")
    List<Long> getAllProductIdByUserId(Long id);
}
