package com.ra.repository;

import com.ra.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ICategoryRepository  extends JpaRepository<Category,Long> {
    Page<Category> findAllByCategoryNameContainingIgnoreCase(Pageable pageable, String name);
    boolean existsByCategoryName(String categoryName);
    boolean findByCategoryName(String name);
    @Modifying
    @Query("update Category c set c.status = case when c.status = true then false else true end where c.id =?1 ")
    void changStatus(Long id);
    @Modifying
    @Query("update Category c set c.status= false where c.id=?1")
    void deleteCategory(Long id);
    Category findCategoryByCategoryName(String name);
    List<Category> findAllByStatus(boolean status);

}
