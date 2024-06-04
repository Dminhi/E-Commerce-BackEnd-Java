package com.ra.controller;

import com.ra.exception.CustomException;
import com.ra.model.dto.mapper.HttpResponse;
import com.ra.model.dto.mapper.ResponseMapper;
import com.ra.model.dto.request.ProductRequestDTO;
import com.ra.model.dto.response.*;
import com.ra.model.entity.*;
import com.ra.repository.*;
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
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ICommentRepository commentRepository;
    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProductDetail(@PathVariable  Long id) throws CustomException {
        Product products =productRepository.findById(id).orElseThrow(() -> new CustomException(("Product not found"),HttpStatus.NOT_FOUND));
        List<Image> images = imageRepository.findByProductId(id);
        List<Comment> comments = commentRepository.findByProductId(id);
        products.setImages(images);
        products.setComments(comments);
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
        return new ResponseEntity<>(new ResponseMapper<>(
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

}