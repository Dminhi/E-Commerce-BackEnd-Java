package com.ra.controller.admin;

import com.ra.config.ConvertPageToPaginationDTO;
import com.ra.model.dto.responsewapper.EHttpStatus;
import com.ra.model.dto.responsewapper.ResponseWapper;
import com.ra.model.entity.Banner;
import com.ra.repository.IDashBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/dashboard")
public class DashboardController {
    @Autowired
    private IDashBoardRepository dashBoardRepository;
    @GetMapping("/user")
    public Integer findUserQuantity() {
        return dashBoardRepository.userQuantity();
    }
    @GetMapping("/productdetail")
    public Integer findProductdetailQuantity() {
        return dashBoardRepository.productDetailQuantity();
    }
    @GetMapping("/order")
    public Integer findOrderQuantity() {
        return dashBoardRepository.orderQuantity();
    }
}
