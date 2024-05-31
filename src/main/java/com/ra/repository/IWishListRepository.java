package com.ra.repository;

import com.ra.model.entity.WishList;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWishListRepository extends JpaRepository<WishList,Long> {
    boolean existsByProductProductIdAndUserId(Long productId,Long userId);

    @Transactional
    void deleteWishListByProductProductIdAndUserId(Long productId,Long userId);

    @Transactional
    void deleteWishListById(Long id);

    List<WishList> findAllByUserId(Long id);
}
