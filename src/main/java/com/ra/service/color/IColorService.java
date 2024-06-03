package com.ra.service.color;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.CategoryRequestDTO;
import com.ra.model.dto.request.ColorRequestDTO;
import com.ra.model.dto.response.ColorResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IColorService {
    ResponseEntity<?> getColor(String keyword, int pageable, int limit, String sort, String order ) throws CustomException;
    Page<ColorResponseDTO> findAllWithPaginationAndSort(Pageable pageable);
    Page<ColorResponseDTO> searchByNameWithPaginationAndSort(Pageable pageable, String name);
    ResponseEntity<?>  save(ColorRequestDTO colorRequestDTO) throws CustomException;
    List<ColorResponseDTO> findAllByStatus(boolean status);
    List<ColorResponseDTO> findAll();
    ResponseEntity<?> edit(Long id, ColorRequestDTO colorRequestDTO) throws CustomException;
    ColorResponseDTO findById(Long id) throws CustomException;
    ResponseEntity<?> getColorById(Long id) throws CustomException;

    ResponseEntity<?> changeStatus(Long id);
}
