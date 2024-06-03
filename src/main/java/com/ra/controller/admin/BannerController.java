package com.ra.controller.admin;

import com.ra.config.ConvertPageToPaginationDTO;
import com.ra.exception.CustomException;
import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.exception.RequestErrorException;
import com.ra.model.dto.request.BannerEditRequest;
import com.ra.model.dto.request.BannerRequest;
import com.ra.model.dto.responsewapper.EHttpStatus;
import com.ra.model.dto.responsewapper.ResponseWapper;
import com.ra.model.entity.Banner;
import com.ra.service.banner.IBannerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/banner")
public class BannerController {
    @Autowired
    private IBannerService bannerService;

    @PostMapping("/add")
    public ResponseEntity<?> addBanner(@ModelAttribute BannerRequest bannerRequest) throws CustomException {
        return bannerService.save(bannerRequest);
    }

    @GetMapping()
    public ResponseEntity<?> findAll(@PageableDefault(page = 0, size = 5, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Banner> banners = bannerService.findAll(pageable);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                ConvertPageToPaginationDTO.convertPageToPaginationDTO(banners)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findBannerById(@PathVariable Long id) throws NotFoundException, DataNotFound {
        Banner banner = bannerService.findBannerById(id);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                banner), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> findUserByUsername(@RequestParam(required = false) String search) throws DataNotFound {
        List<Banner> banners = bannerService.findBannerByBannername(search);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                banners), HttpStatus.OK);    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id) throws NotFoundException, RequestErrorException {
        Banner banner = bannerService.changeStatus(id);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                banner), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@Valid @ModelAttribute BannerEditRequest bannerEditRequest) throws NotFoundException, RequestErrorException {
        Banner bannerEdit = bannerService.updateBanner(bannerEditRequest, id);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                bannerEdit), HttpStatus.OK);    }

    @GetMapping("/true")
    public ResponseEntity<?> findAllByStatusTrue(@PageableDefault(page = 0, size = 5, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Banner> banners = bannerService.findAllByStatusTrue(pageable);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                ConvertPageToPaginationDTO.convertPageToPaginationDTO(banners)), HttpStatus.OK);
    }
}
