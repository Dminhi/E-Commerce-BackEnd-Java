package com.ra.repository;

import com.ra.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IDashBoardRepository extends JpaRepository<User,Long>{
    @Query("select COUNT(*) from User")
    Integer userQuantity();
    @Query("SELECT COUNT(*) FROM Orders WHERE MONTH(createdAt) = MONTH(now()) AND YEAR(createdAt) = YEAR(now())")
    Integer orderQuantity();
    @Query("select COUNT(*) from ProductDetail ")
    Integer productDetailQuantity();
}
