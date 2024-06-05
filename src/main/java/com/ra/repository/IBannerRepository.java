package com.ra.repository;

import com.ra.model.entity.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IBannerRepository extends JpaRepository<Banner,Long> {
    List<Banner> findAllByBannerNameContains(String search);
    Page<Banner> findAllByStatusIsTrue(Pageable pageable);
    boolean existsByBannerName(String userName);
}
