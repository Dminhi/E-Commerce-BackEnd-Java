package com.ra.controller.admin;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.ColorRequestDTO;
import com.ra.model.dto.request.ConfigRequestDTO;
import com.ra.service.color.IColorService;
import com.ra.service.config.IConfigServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api.myservice.com/v1/admin/colors")
public class ColorController {
    @Autowired
    private IColorService colorService;
    @GetMapping("")
    public ResponseEntity<?> getConfig(@RequestParam(name = "keyword", required = false) String keyword,
                                       @RequestParam(defaultValue = "5", name = "limit") int limit,
                                       @RequestParam(defaultValue = "0", name = "page") int page,
                                       @RequestParam(defaultValue = "id", name = "sort") String sort,
                                       @RequestParam(defaultValue = "asc", name = "order") String order) throws CustomException {
        return colorService.getColor(keyword, page, limit, sort, order);

    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getConfigById(@PathVariable Long id) throws CustomException {
        return colorService.getColorById(id);
    }
    @PostMapping("")
    public ResponseEntity<?> addConfig ( @Valid @RequestBody ColorRequestDTO colorRequestDTO) throws CustomException {
        return colorService.save(colorRequestDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> editConfig (@PathVariable Long id, @Valid @RequestBody  ColorRequestDTO colorRequestDTO) throws CustomException {
        return colorService.edit(id,colorRequestDTO);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<?> changeStatus (@PathVariable Long id) throws CustomException {
        return colorService.changeStatus(id);
    }
}
