package com.ra.service.banner;

import com.ra.exception.CustomException;
import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.exception.RequestErrorException;
import com.ra.model.dto.mapper.HttpResponse;
import com.ra.model.dto.mapper.ResponseMapper;
import com.ra.model.dto.request.BannerEditRequest;
import com.ra.model.dto.request.BannerRequest;
import com.ra.model.entity.Banner;
import com.ra.repository.IBannerRepository;
import com.ra.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class BannerServiceImpl implements IBannerService{
    @Autowired
    private IBannerRepository bannerRepository;
    @Autowired
    private UploadService uploadService;

    @Override
    public ResponseEntity<?> save(BannerRequest bannerRequest) throws CustomException {
        if(bannerRequest.getId() ==null) {
            if(bannerRepository.existsByBannerName(bannerRequest.getBannerName())) {
                throw new CustomException("Banner name has been already existed!", HttpStatus.CONFLICT);
            }
        }
        String imageUrl = uploadService.uploadFileToServer(bannerRequest.getFile());

        Banner banner = bannerRepository.save(Banner.builder()
                .id(bannerRequest.getId())
                .bannerName(bannerRequest.getBannerName())
                .description(bannerRequest.getDescription())
                .image(imageUrl)
                .status(bannerRequest.isStatus())
                .createdAt(LocalDate.now())
                .build());

        Map<String, Banner> responseDTOMap = new HashMap<>();
        responseDTOMap.put("Add new banner successfully", banner);
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        responseDTOMap), HttpStatus.CREATED);
    }

    @Override
    public Page<Banner> findAll(Pageable pageable) {
        return bannerRepository.findAll(pageable);
    }

    @Override
    public Banner changeStatus(Long id) throws NotFoundException, RequestErrorException {
        Banner banner = bannerRepository.findById(id).orElseThrow(()->new NotFoundException("banner not found"));
        banner.setStatus(!banner.isStatus());
        bannerRepository.save(banner);
        return banner;
    }

    @Override
    public Banner updateBanner(BannerEditRequest bannerEditRequest,Long id) throws NotFoundException, RequestErrorException {
        Banner banner = bannerRepository.findById(bannerEditRequest.getId()).orElseThrow(()->new NotFoundException("banner not found"));
        banner.setBannerName(bannerEditRequest.getBannerName());
        if(!Objects.equals(banner.getBannerName(), bannerEditRequest.getBannerName())){
            if(bannerRepository.existsByBannerName(bannerEditRequest.getBannerName())){
                throw new RequestErrorException("banner name exist");
            }
            else {
                banner.setBannerName(bannerEditRequest.getBannerName());}
        }
        banner.setDescription(bannerEditRequest.getDescription());
        banner.setCreatedAt(LocalDate.now());
        banner.setImage(uploadService.uploadFileToServer(bannerEditRequest.getFile()));
        bannerRepository.save(banner);
        return banner;
    }

    @Override
    public Banner findBannerById(Long id) throws NotFoundException {
        return bannerRepository.findById(id).orElseThrow(() -> new NotFoundException("banner not found"));
    }

    @Override
    public Page<Banner> findAllByStatusTrue(Pageable pageable) {
        return bannerRepository.findAllByStatusIsTrue(pageable);
    }

    @Override
    public Page<Banner> findAllByBannerName(String search, Pageable pageable) throws DataNotFound {
        Page<Banner> findBannerByBannername = bannerRepository.findAllByBannerNameContains(search,pageable);
        if(findBannerByBannername.isEmpty()){throw new DataNotFound("banner is empty");}
        return findBannerByBannername;}
    }

