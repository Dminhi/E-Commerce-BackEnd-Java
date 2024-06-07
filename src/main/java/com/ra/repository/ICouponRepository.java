package com.ra.repository;

import com.ra.model.entity.Coupons;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICouponRepository extends JpaRepository<Coupons,Long> {

}
