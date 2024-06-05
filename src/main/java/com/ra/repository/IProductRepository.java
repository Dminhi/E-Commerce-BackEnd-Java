package com.ra.repository;

import com.ra.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT p FROM Product p JOIN p.productDetails pd WHERE " +
            "(:brand IS NULL OR LOWER(p.brand.brandName) LIKE LOWER(CONCAT('%', :brand, '%'))) AND " +
            "(:category IS NULL OR LOWER(p.category.categoryName) LIKE LOWER(CONCAT('%', :category, '%'))) AND " +
            "(:minPrice IS NULL OR pd.unitPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR pd.unitPrice <= :maxPrice)")
    List<Product> searchProducts(@Param("brand") String brand,
                                 @Param("category") String category,
                                 @Param("minPrice") Double minPrice,
                                 @Param("maxPrice") Double maxPrice);
}
