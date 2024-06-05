package com.ra.controller;

import com.ra.exception.CustomException;
import com.ra.model.dto.mapper.HttpResponse;
import com.ra.model.dto.mapper.ResponseMapper;
import com.ra.model.dto.request.ProductRequestDTO;
import com.ra.model.dto.request.SearchCriteria;
import com.ra.model.dto.response.*;
import com.ra.model.entity.Brand;
import com.ra.model.entity.Category;
import com.ra.model.entity.Color;
import com.ra.model.entity.Product;
import com.ra.repository.IBrandRepository;
import com.ra.repository.ICategoryRepository;
import com.ra.repository.IColorRepository;
import com.ra.repository.IProductRepository;
import com.ra.service.product.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1")
public class HomeController {
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IProductService productService;
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private IBrandRepository brandRepository;
    @Autowired
    private IColorRepository colorRepository;
    @GetMapping("/products/new-products")
    public ResponseEntity<?> getProduct() throws CustomException {
        List<Product> products =productRepository.findAll();
        List<HomeProductResponseDTO>responseDTOMap = products.stream().map(HomeProductResponseDTO::new).toList();
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.OK.value(),
                        HttpStatus.OK.name(),
                        responseDTOMap), HttpStatus.OK);

    }
    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProductDetail(@PathVariable  Long id) throws CustomException {
        Product products =productRepository.findById(id).orElseThrow(() -> new CustomException(("Product not found"),HttpStatus.NOT_FOUND));
        HomeProductResponseDTO responseDTOMap = new HomeProductResponseDTO(products);
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.OK.value(),
                        HttpStatus.OK.name(),
                        responseDTOMap), HttpStatus.OK);

    }
    @GetMapping("/categories/new-categories")
    public ResponseEntity<?> getCategory() throws CustomException {
        List<Category> categories =categoryRepository.findAll();
        List<CategoryResponseDTO>responseDTOMap = categories.stream().map(CategoryResponseDTO::new).toList();
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.OK.value(),
                        HttpStatus.OK.name(),
                        responseDTOMap), HttpStatus.OK);

    }
    @GetMapping("/brands/new-brands")
    public ResponseEntity<?> Brand() throws CustomException {
        List<Brand> brandList =brandRepository.findAll();
        List<BrandResponseDTO>responseDTOMap = brandList.stream().map(BrandResponseDTO::new).toList();
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.OK.value(),
                        HttpStatus.OK.name(),
                        responseDTOMap), HttpStatus.OK);

    }
    @GetMapping("/colors")
    public ResponseEntity<?> Color() throws CustomException {
        List<Color> colorList =colorRepository.findAll();
        List<ColorResponseDTO>responseDTOMap = colorList.stream().map(ColorResponseDTO::new).toList();
        return new ResponseEntity<>(
                new ResponseMapper<>(
                        HttpResponse.SUCCESS,
                        HttpStatus.OK.value(),
                        HttpStatus.OK.name(),
                        responseDTOMap), HttpStatus.OK);

    }
    @PostMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProducts(@RequestBody SearchCriteria searchCriteria) {
        List<ProductResponseDTO> products = productService.searchProducts(
                searchCriteria.getBrand(),
                searchCriteria.getCategory(),
                searchCriteria.getMinPrice(),
                searchCriteria.getMaxPrice());
        return ResponseEntity.ok(products);
    }
}
