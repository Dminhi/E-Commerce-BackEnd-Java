package com.ra.repository;

import com.ra.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IDashBoardRepository extends JpaRepository<User,Long>{
    @Query("SELECT COUNT(*) FROM Orders")
    Integer orderQuantity();
    @Query(value = "SELECT SUM(totalPriceAfterCoupons) AS totalSum FROM Orders o WHERE o.status = 'SUCCESS'",nativeQuery = true)
    Integer revenue();
    @Query(value = "SELECT COUNT(od.productDetail_id) AS totalProductDetails FROM OrderDetail od JOIN Orders o ON od.orders_id = o.id WHERE o.status = 'SUCCESS'",nativeQuery = true)
    Integer productDetailOrder();
}
