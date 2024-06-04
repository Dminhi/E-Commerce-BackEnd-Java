package com.ra.service.product;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.ProductEditRequestDTO;
import com.ra.model.dto.request.ProductRequestDTO;
import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IProductService {
    ResponseEntity<?> getProduct(String keyword, int page, int limit, String sort, String order) throws CustomException;
    Page<ProductResponseDTO> findAllWithPaginationAndSort(Pageable pageable);

    Page<ProductResponseDTO> searchByNameWithPaginationAndSort( String name,Pageable pageable);

    ProductResponseDTO findById(Long id) throws CustomException;

    ResponseEntity<?> save(ProductRequestDTO productRequestDTO) throws CustomException, CustomException;

    ResponseEntity<?> changeStatus(Long id) throws CustomException;

    List<ProductResponseDTO> findAllByStatus(boolean status);

    ResponseEntity<?> editProduct(Long id, ProductEditRequestDTO productEditRequestDTO) throws  CustomException;
    List<ProductResponseDTO> searchProducts(String brand, String category, Double minPrice, Double maxPrice);


}
