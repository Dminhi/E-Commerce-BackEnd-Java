package com.ra.service.productDetail;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.ProductDetailRequestDTO;
import com.ra.model.dto.request.ProductEditRequestDTO;
import com.ra.model.dto.request.ProductRequestDTO;
import com.ra.model.dto.response.ProductDetailResponseDTO;
import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.model.entity.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IProductDetailService {
    ResponseEntity<?> getProductDetail(String keyword, int page, int limit, String sort, String order) throws CustomException;
    Page<ProductDetailResponseDTO> findAllWithPaginationAndSort(Pageable pageable);

    Page<ProductDetailResponseDTO> searchByNameWithPaginationAndSort( String name,Pageable pageable);

    ProductDetailResponseDTO findById(Long id) throws CustomException;

    ResponseEntity<?> save(ProductDetailRequestDTO productDetailRequestDTO) throws CustomException;

    ResponseEntity<?> changeStatus(Long id) throws CustomException;

    List<ProductDetailResponseDTO> findAllByStatus(boolean status);

    ResponseEntity<?> editProduct(Long id, ProductDetailRequestDTO productDetailRequestDTO) throws  CustomException;

    ProductDetail findProductDetailById(Long id) throws CustomException;


}
