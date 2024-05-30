package com.ra.service.brand;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.BrandRequestDTO;
import com.ra.model.dto.response.BrandResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IBrandService {
    ResponseEntity<?> getBrand(String keyword, int pageable, int limit, String sort, String order ) throws CustomException;

    Page<BrandResponseDTO> findAllWithPaginationAndSort(Pageable pageable);

    Page<BrandResponseDTO> searchByNameWithPaginationAndSort(Pageable pageable, String name);
    ResponseEntity<?>  save(BrandRequestDTO brandRequestDTO) throws CustomException;
    List<BrandResponseDTO> findAllByStatus(boolean status);
    List<BrandResponseDTO> findAll();
    ResponseEntity<?> edit(Long id, BrandRequestDTO brandRequestDTO) throws CustomException;
    BrandResponseDTO findById(Long id) throws CustomException;
    ResponseEntity<?> getBrandById(Long id) throws CustomException;

    ResponseEntity<?> changeStatus(Long id);
}
