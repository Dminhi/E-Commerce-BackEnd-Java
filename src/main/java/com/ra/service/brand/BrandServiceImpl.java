package com.ra.service.brand;

import com.ra.exception.CustomException;
import com.ra.model.dto.mapper.HttpResponse;
import com.ra.model.dto.mapper.PageDataDTO;
import com.ra.model.dto.mapper.ResponseMapper;
import com.ra.model.dto.request.BrandRequestDTO;
import com.ra.model.dto.response.BrandResponseDTO;
import com.ra.model.dto.response.CategoryResponseDTO;
import com.ra.model.entity.Brand;
import com.ra.model.entity.Category;
import com.ra.repository.IBrandRepository;
import com.ra.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements IBrandService{
    @Autowired
    private IBrandRepository brandRepository;
    @Autowired
    private UploadService uploadService;
    @Override
    public ResponseEntity<?> getBrand(String keyword, int page, int limit, String sort, String order) throws CustomException {
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sort));
        Page<BrandResponseDTO> brandPage;
        if (keyword != null && !keyword.isEmpty()) {
            brandPage = searchByNameWithPaginationAndSort(pageable, keyword);
        } else {
            brandPage = findAllWithPaginationAndSort(pageable);
        }
        if (brandPage == null || brandPage.isEmpty()) {
            throw new CustomException("Category is not found", HttpStatus.NOT_FOUND);

        }
        PageDataDTO<BrandResponseDTO> pageDataDTO = new PageDataDTO<>();
        pageDataDTO.setCurrentPage(brandPage.getNumber());
        pageDataDTO.setTotalPage(brandPage.getTotalPages());
        pageDataDTO.setLimit(brandPage.getSize());
        pageDataDTO.setSort(sort);
        pageDataDTO.setTotalElement(brandPage.getTotalElements());
        pageDataDTO.setSearchName(keyword == null ? "" : keyword);
        pageDataDTO.setContent(brandPage.getContent());
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                pageDataDTO
        ), HttpStatus.OK);
    }

    @Override
    public Page<BrandResponseDTO> findAllWithPaginationAndSort(Pageable pageable) {
        Page<Brand> list = brandRepository.findAll(pageable);
        return list.map(BrandResponseDTO::new);
    }

    @Override
    public Page<BrandResponseDTO> searchByNameWithPaginationAndSort(Pageable pageable, String name) {
        Page<Brand> list = brandRepository.findAllByBrandNameContainingIgnoreCase(pageable, name);
        return list.map(BrandResponseDTO::new);
    }

    @Override
    public ResponseEntity<?> save(BrandRequestDTO brandRequestDTO) throws CustomException {
        if(brandRequestDTO.getId() ==null) {
            if(brandRepository.existsByBrandName(brandRequestDTO.getBrandName())) {
                throw new CustomException("Category name has been already existed!", HttpStatus.CONFLICT);
            }
        }
        String imageUrl = uploadService.uploadFileToServer(brandRequestDTO.getFile());

        Brand brand = brandRepository.save(Brand.builder()
                .id(brandRequestDTO.getId())
                .brandName(brandRequestDTO.getBrandName())
                .description(brandRequestDTO.getDescription())
                .image(imageUrl)
                .status(brandRequestDTO.isStatus())
                .build());

        Map<String, Brand> responseDTOMap = new HashMap<>();
        responseDTOMap.put("Add new Brand successfully", brand);
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        responseDTOMap), HttpStatus.CREATED);
    }

    @Override
    public List<BrandResponseDTO> findAllByStatus(boolean status) {
        List<Brand> brands = brandRepository.findAllByStatus(status);
        return brands.stream().map(BrandResponseDTO::new).toList();
    }

    @Override
    public List<BrandResponseDTO> findAll() {
        List<Brand> brands = brandRepository.findAll();
        return brands.stream().map(BrandResponseDTO::new).toList();
    }

    @Override
    public ResponseEntity<?> edit(Long id, BrandRequestDTO brandRequestDTO) throws CustomException {
        brandRequestDTO.setId(id);
        BrandResponseDTO editBrandResponseDTO = findById(brandRequestDTO.getId());
        boolean brandNameExist = brandRepository.existsByBrandName(brandRequestDTO.getBrandName());

        if (brandNameExist) {
            throw new CustomException("Brand name has been already existed", HttpStatus.CONFLICT);
        }
        String imageUrl = editBrandResponseDTO.getImage();
        if(!brandRequestDTO.getFile().isEmpty()) {
            imageUrl = uploadService.uploadFileToServer(brandRequestDTO.getFile());
        }

        Brand brand = brandRepository.save(Brand.builder()
                .id(brandRequestDTO.getId())
                .brandName(brandRequestDTO.getBrandName())
                .description(brandRequestDTO.getDescription())
                .image(imageUrl)
                .status(brandRequestDTO.isStatus())
                .build());
        Map<String, Brand> responseDTOMap = new HashMap<>();
        responseDTOMap.put("Edit Brand successfully", brand);
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        responseDTOMap), HttpStatus.CREATED);
    }

    @Override
    public BrandResponseDTO findById(Long id) throws CustomException {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new CustomException("Brand is not found!!" + id, HttpStatus.NOT_FOUND));

        return BrandResponseDTO.builder()
                .id(brand.getId())
                .brandName(brand.getBrandName())
                .description(brand.getDescription())
                .image(brand.getImage())
                .status(brand.isStatus())
                .products(brand.getProducts())
                .build()
                ;
    }

    @Override
    public ResponseEntity<?> getBrandById(Long id) throws CustomException {
        BrandResponseDTO categoryPage = findById(id);
        return new ResponseEntity<>(new ResponseMapper<>(
                HttpResponse.SUCCESS,
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                categoryPage
        ), HttpStatus.OK);
    }
}
