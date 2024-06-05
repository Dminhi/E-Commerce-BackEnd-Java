package com.ra.service.banner;

import com.ra.exception.CustomException;
import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.exception.RequestErrorException;
import com.ra.model.dto.request.BannerEditRequest;
import com.ra.model.dto.request.BannerRequest;
import com.ra.model.dto.request.CategoryRequestDTO;
import com.ra.model.entity.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IBannerService {
    Page<Banner> findAll(Pageable pageable);
    Banner changeStatus(Long id) throws NotFoundException, RequestErrorException;

    List<Banner> findBannerByBannername(String search) throws DataNotFound;

    Banner updateBanner(BannerEditRequest bannerEditRequest,Long id) throws NotFoundException, RequestErrorException;
    Banner findBannerById(Long id) throws NotFoundException;
    ResponseEntity<?> save(BannerRequest bannerRequest) throws CustomException;

    Page<Banner> findAllByStatusTrue(Pageable pageable);
}
