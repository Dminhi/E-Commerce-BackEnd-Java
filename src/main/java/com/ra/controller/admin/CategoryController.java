package com.ra.controller.admin;

import com.ra.exception.CustomException;
import com.ra.model.dto.mapper.HttpResponse;
import com.ra.model.dto.mapper.PageDataDTO;
import com.ra.model.dto.mapper.ResponseMapper;
import com.ra.model.dto.request.CategoryRequestDTO;
import com.ra.model.dto.response.CategoryResponseDTO;
import com.ra.service.category.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/categories")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;
    @GetMapping("")
    public ResponseEntity<?> getCategory(@RequestParam(name = "keyword", required = false) String keyword,
                                         @RequestParam(defaultValue = "5", name = "limit") int limit,
                                         @RequestParam(defaultValue = "0", name = "page") int page,
                                         @RequestParam(defaultValue = "id", name = "sort") String sort,
                                         @RequestParam(defaultValue = "asc", name = "order") String order) throws CustomException {
        return categoryService.getCategory(keyword, page, limit, sort, order);

    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) throws CustomException {
        return categoryService.getCategoryById(id);
    }
    @PostMapping("")
    public ResponseEntity<?> addCategory ( @Valid @ModelAttribute("category")  CategoryRequestDTO categoryRequestDTO) throws CustomException {
        return categoryService.save(categoryRequestDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> editCategory (@PathVariable Long id, @Valid @ModelAttribute("category")  CategoryRequestDTO categoryRequestDTO) throws CustomException {
        return categoryService.edit(id,categoryRequestDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> changeStatus (@PathVariable Long id) throws CustomException {
        return categoryService.changeStatus(id);
    }
}
