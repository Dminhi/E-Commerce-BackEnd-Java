package com.ra.controller.admin;

import com.ra.exception.CustomException;
import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.model.dto.request.ProductDetailRequestDTO;
import com.ra.model.dto.responsewapper.EHttpStatus;
import com.ra.model.dto.responsewapper.ResponseWapper;
import com.ra.model.entity.Banner;
import com.ra.model.entity.ProductDetail;
import com.ra.service.productDetail.IProductDetailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/productDetail")
public class ProductDetailController {
    @Autowired
    private IProductDetailService productDetailService;
    @GetMapping("")
    public ResponseEntity<?> getProduct(@RequestParam(name = "keyword", required = false) String keyword,
                                        @RequestParam(defaultValue = "5", name = "limit") int limit,
                                        @RequestParam(defaultValue = "0", name = "page") int page,
                                        @RequestParam(defaultValue = "id", name = "sort") String sort,
                                        @RequestParam(defaultValue = "asc", name = "order") String order) throws CustomException {
        return productDetailService.getProductDetail(keyword, page, limit, sort, order);
    }
    @PostMapping("")
    public ResponseEntity<?> addProduct(@Valid @ModelAttribute("product") ProductDetailRequestDTO productDetailRequestDTO) throws CustomException {
        return productDetailService.save(productDetailRequestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editProduct(@PathVariable Long id,@Valid @ModelAttribute("product") ProductDetailRequestDTO productDetailRequestDTO) throws CustomException {
        return productDetailService.editProduct(id,productDetailRequestDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> changeStatus (@PathVariable Long id) throws CustomException {
        return productDetailService.changeStatus(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findProductDetailById(@PathVariable Long id) throws CustomException {
        ProductDetail productDetail = productDetailService.findProductDetailById(id);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                productDetail), HttpStatus.OK);
    }

}
