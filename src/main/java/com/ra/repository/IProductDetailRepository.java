package com.ra.repository;

import com.ra.model.entity.Category;
import com.ra.model.entity.Product;
import com.ra.model.entity.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface IProductDetailRepository extends JpaRepository<ProductDetail,Long> {
    Page<ProductDetail> findProductDetailByProductDetailName(String name, Pageable pageable);
    boolean existsByProductDetailName(String productDetailName);
    Page<ProductDetail> findAllByProductDetailNameContainingIgnoreCase( String name,Pageable pageable);

    @Modifying
    @Query("update Product p set p.status=case when p.status = true then false else true end where p.id=?1")
    void changeStatus(Long id);
    List<ProductDetail> findAllByStatus(boolean status);
}
