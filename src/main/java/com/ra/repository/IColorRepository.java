package com.ra.repository;

import com.ra.model.entity.Color;
import com.ra.model.entity.Config;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface IColorRepository extends JpaRepository<Color,Long> {
    Page<Color> findAllByColorNameContainingIgnoreCase(Pageable pageable, String name);
    boolean existsByColorName(String colorName);
    boolean findByColorName(String name);
    @Modifying
    @Query("update Color c set c.status = case when c.status = true then false else true end where c.id =?1 ")
    void changStatus(Long id);

    Color findConfigByColorName(String name);
    List<Color> findAllByStatus(boolean status);
}
