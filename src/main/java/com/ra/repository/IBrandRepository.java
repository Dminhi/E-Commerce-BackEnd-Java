package com.ra.repository;

import com.ra.model.entity.Brand;
import com.ra.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IBrandRepository extends JpaRepository<Brand,Long> {

    Page<Brand> findAllByBrandNameContainingIgnoreCase(Pageable pageable, String name);

    boolean existsByBrandName(String brandName);
    boolean findByBrandName(String brandName);

    @Modifying
    @Query("update Brand b set b.status = case when b.status = true then false else true end where b.id =?1 ")
    void changStatus(Long id);
    @Modifying
    @Query("update Brand b set b.status= false where b.id=?1")
    void deleteBrand(Long id);

    Brand findBrandByBrandName(String name);
    List<Brand> findAllByStatus(boolean status);

}
