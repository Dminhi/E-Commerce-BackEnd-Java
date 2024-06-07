package com.ra.service.coupon;

import com.ra.exception.NotFoundException;
import com.ra.model.entity.Coupons;

public interface ICouponService {
    Coupons findById(Long id) throws NotFoundException;
}
