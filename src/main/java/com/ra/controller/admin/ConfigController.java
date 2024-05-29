package com.ra.controller.admin;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.CategoryRequestDTO;
import com.ra.model.dto.request.ConfigRequestDTO;
import com.ra.service.category.ICategoryService;
import com.ra.service.config.IConfigServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/configs")
public class ConfigController {
    @Autowired
    private IConfigServiceImpl configService;
    @GetMapping("")
    public ResponseEntity<?> getConfig(@RequestParam(name = "keyword", required = false) String keyword,
                                         @RequestParam(defaultValue = "5", name = "limit") int limit,
                                         @RequestParam(defaultValue = "0", name = "page") int page,
                                         @RequestParam(defaultValue = "id", name = "sort") String sort,
                                         @RequestParam(defaultValue = "asc", name = "order") String order) throws CustomException {
        return configService.getConfig(keyword, page, limit, sort, order);

    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getConfigById(@PathVariable Long id) throws CustomException {
        return configService.getConfigById(id);
    }
    @PostMapping("")
    public ResponseEntity<?> addConfig ( @Valid @RequestBody ConfigRequestDTO configRequestDTO) throws CustomException {
        return configService.save(configRequestDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> editConfig (@PathVariable Long id, @Valid @RequestBody  ConfigRequestDTO configRequestDTO) throws CustomException {
        return configService.edit(id,configRequestDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> changeStatus (@PathVariable Long id) throws CustomException {
        return configService.changeStatus(id);
    }
}
