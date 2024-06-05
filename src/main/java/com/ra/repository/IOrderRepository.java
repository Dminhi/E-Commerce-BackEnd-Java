package com.ra.repository;

import com.ra.model.entity.Orders;
import com.ra.model.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Orders,Long> {
    Page<Orders> getOrdersByUserId(Long id,Pageable pageable);
    Orders getOrdersBySerialNumber(String serialNumber);

    Page<Orders> findAllByStatus(Status status, Pageable pageable);

    List<Orders> findAllByStatusAndUserId(Status status,Long id);

}
