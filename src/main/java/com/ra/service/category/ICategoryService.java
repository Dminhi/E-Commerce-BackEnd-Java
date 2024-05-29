package com.ra.service.category;

import com.ra.exception.CustomException;
import com.ra.model.dto.mapper.PageDataDTO;
import com.ra.model.dto.request.CategoryRequestDTO;
import com.ra.model.dto.response.CategoryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICategoryService {
    ResponseEntity<?> getCategory(String keyword, int pageable, int limit, String sort, String order ) throws CustomException;
    Page<CategoryResponseDTO> findAllWithPaginationAndSort(Pageable pageable);
    Page<CategoryResponseDTO> searchByNameWithPaginationAndSort(Pageable pageable, String name);
    ResponseEntity<?>  save(CategoryRequestDTO categoryRequestDTO) throws CustomException;
    List<CategoryResponseDTO> findAllByStatus(boolean status);
    List<CategoryResponseDTO> findAll();
    ResponseEntity<?> edit(Long id, CategoryRequestDTO categoryRequestDTO) throws CustomException;
    CategoryResponseDTO findById(Long id) throws CustomException;
    ResponseEntity<?> getCategoryById(Long id) throws CustomException;
    ResponseEntity<?> changeStatus(Long id) throws CustomException;

}
