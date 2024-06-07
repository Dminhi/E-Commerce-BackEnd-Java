package com.ra.service.coupon;

import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.model.entity.Coupons;
import com.ra.repository.ICouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl implements ICouponService{

    @Autowired
    private ICouponRepository couponRepository;
    @Override
    public Coupons findById(Long id) throws NotFoundException {
        return couponRepository.findById(id).orElseThrow(() -> new NotFoundException("coupon not found"));
    }
}
