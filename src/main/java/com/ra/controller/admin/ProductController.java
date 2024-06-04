package com.ra.controller.admin;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.ProductEditRequestDTO;
import com.ra.model.dto.request.ProductRequestDTO;
import com.ra.service.product.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/products")
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping("")
    public ResponseEntity<?> getProduct(@RequestParam(name = "keyword", required = false) String keyword,
                                        @RequestParam(defaultValue = "5", name = "limit") int limit,
                                        @RequestParam(defaultValue = "0", name = "page") int page,
                                        @RequestParam(defaultValue = "id", name = "sort") String sort,
                                        @RequestParam(defaultValue = "asc", name = "order") String order) throws CustomException {
        return productService.getProduct(keyword, page, limit, sort, order);
    }
    @PostMapping("")
    public ResponseEntity<?> addProduct(@Valid @ModelAttribute("product")ProductRequestDTO productRequestDTO) throws CustomException {
        return productService.save(productRequestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editProduct(@PathVariable Long id,@Valid @ModelAttribute("product") ProductEditRequestDTO productEditRequestDTO) throws CustomException {
        return productService.editProduct(id,productEditRequestDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> changeStatus (@PathVariable Long id) throws CustomException {
        return productService.changeStatus(id);
    }


}
