package com.ra.repository;

import com.ra.model.entity.ShoppingCart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    Optional<ShoppingCart> findByProductDetailIdAndUserId(Long productDetailId,Long userId);

    @Transactional
    void deleteShoppingCartByUserId(Long id);

    List<ShoppingCart> findAllByUserId(Long id);

}
