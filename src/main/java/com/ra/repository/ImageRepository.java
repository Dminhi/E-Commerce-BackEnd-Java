package com.ra.repository;

import com.ra.model.entity.Color;
import com.ra.model.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ImageRepository extends JpaRepository<Image,Long> {

    @Query("select i from Image i where i.product.id = :productId")
    List<Image> findByProductId(@Param("productId") Long productId);

}
