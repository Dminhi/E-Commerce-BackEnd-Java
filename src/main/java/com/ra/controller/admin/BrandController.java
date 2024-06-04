package com.ra.controller.admin;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.BrandRequestDTO;
import com.ra.service.brand.IBrandService;
import com.ra.model.dto.request.CategoryRequestDTO;
import com.ra.service.brand.IBrandService;
import com.ra.service.category.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/brands")
public class BrandController {
    @Autowired
    private IBrandService brandService;
    @GetMapping("")
    public ResponseEntity<?> getBrand(@RequestParam(name = "keyword", required = false) String keyword,
                                         @RequestParam(defaultValue = "5", name = "limit") int limit,
                                         @RequestParam(defaultValue = "0", name = "page") int page,
                                         @RequestParam(defaultValue = "id", name = "sort") String sort,
                                         @RequestParam(defaultValue = "asc", name = "order") String order) throws CustomException {
        return brandService.getBrand(keyword, page, limit, sort, order);

    }
    @GetMapping("/{id}")

    public ResponseEntity<?> getBrandById(@PathVariable Long id) throws CustomException {

        return brandService.getBrandById(id);
    }

    @PostMapping("")

    public ResponseEntity<?> addBrand ( @Valid @ModelAttribute("brand") BrandRequestDTO brandRequestDTO) throws CustomException {

        return brandService.save(brandRequestDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> editBrand (@PathVariable Long id, @Valid @ModelAttribute("brand")  BrandRequestDTO brandRequestDTO) throws CustomException {
        return brandService.edit(id,brandRequestDTO);

    }
    @PatchMapping("/{id}")
    public ResponseEntity<?> changeStatus (@PathVariable Long id) throws CustomException {
        return brandService.changeStatus(id);

    }
}
