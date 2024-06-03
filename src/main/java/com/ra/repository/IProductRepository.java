package com.ra.repository;

import com.ra.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface IProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findProductByProductName(String name, Pageable pageable);

    Page<Product> findAllByProductNameContainingIgnoreCase( String name,Pageable pageable);

    boolean existsByProductName(String name);
    List<Product> findAllByStatus(boolean status);
    @Modifying
    @Query("update Product p set p.status=case when p.status = true then false else true end where p.id=?1")
    void changeStatus(Long id);
    @Modifying
    @Query("SELECT p FROM Product p ORDER BY p.createdAt DESC")
    List<Product> findByOrderByCreatedAtDesc();
}
