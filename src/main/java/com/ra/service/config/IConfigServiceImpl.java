package com.ra.service.config;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.CategoryRequestDTO;
import com.ra.model.dto.request.ConfigRequestDTO;
import com.ra.model.dto.response.CategoryResponseDTO;
import com.ra.model.dto.response.ConfigResponseDTO;
import com.ra.model.entity.Config;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IConfigServiceImpl {
    ResponseEntity<?> getConfig(String keyword, int pageable, int limit, String sort, String order ) throws CustomException;
    Page<ConfigResponseDTO> findAllWithPaginationAndSort(Pageable pageable);
    Page<ConfigResponseDTO> searchByNameWithPaginationAndSort(Pageable pageable, String name);
    ResponseEntity<?>  save(ConfigRequestDTO configRequestDTO) throws CustomException;
    List<ConfigResponseDTO> findAllByStatus(boolean status);
    List<ConfigResponseDTO> findAll();
    ResponseEntity<?> edit(Long id, ConfigRequestDTO configRequestDTO) throws CustomException;
    ConfigResponseDTO findById(Long id) throws CustomException;
    ResponseEntity<?> getConfigById(Long id) throws CustomException;

    ResponseEntity<?> changeStatus(Long id);
}
