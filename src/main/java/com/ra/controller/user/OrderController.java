package com.ra.controller.user;

import com.ra.config.ConvertPageToPaginationDTO;
import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.model.dto.response.OrderResponse;
import com.ra.model.dto.responsewapper.EHttpStatus;
import com.ra.model.dto.responsewapper.ResponseWapper;
import com.ra.service.order.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/user/history")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @GetMapping("")
    public ResponseEntity<?> findAll(@PageableDefault(page = 0, size = 5, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) throws NotFoundException, DataNotFound {
        Page<OrderResponse> orderResponses = orderService.getOrdersByUserId(pageable);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                orderResponses), HttpStatus.OK);
    }

    @GetMapping("/{serialNumber}")
    public ResponseEntity<?> getDetailOrderBySerialNumber(@PathVariable String serialNumber) throws NotFoundException, DataNotFound {
        OrderResponse orderResponse = orderService.getOrderResponseBySerialNumber(serialNumber);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                orderResponse), HttpStatus.OK);
    }

    @GetMapping("order/{orderStatus}")
    public ResponseEntity<?> getOrderByOrderStatus(@PathVariable String orderStatus, @PageableDefault(page = 0, size = 5, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) throws NotFoundException, DataNotFound {
        Page<OrderResponse> orderResponse = orderService.getOrderResponseByOrderStatusByUser(orderStatus, pageable);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                ConvertPageToPaginationDTO.convertPageToPaginationDTO(orderResponse)), HttpStatus.OK);
    }
}
