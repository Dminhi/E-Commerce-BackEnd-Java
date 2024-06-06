package com.ra.controller.admin;


import com.ra.config.ConvertPageToPaginationDTO;
import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.model.dto.response.OrderResponse;
import com.ra.model.dto.responsewapper.EHttpStatus;
import com.ra.model.dto.responsewapper.ResponseWapper;
import com.ra.model.entity.Status;
import com.ra.service.order.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/orders")
public class OrdersController {
    @Autowired
    private IOrderService orderService;


    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderByOrderId(@PathVariable Long orderId) throws DataNotFound, NotFoundException {
        OrderResponse orderResponse = orderService.getOrderByOrderId(orderId);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                orderResponse), HttpStatus.OK);}

    @PutMapping("/status")
    public ResponseEntity<?> updateOrderStatus(@RequestParam String orderStatus,Long orderId) throws NotFoundException {
        OrderResponse orderResponse = orderService.updateOrderStatus(orderId, Status.valueOf(orderStatus));
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                orderResponse), HttpStatus.OK);    }

    @GetMapping("/order/{orderStatus}")
    public ResponseEntity<?> findAllOrderByOrderStatus(@PathVariable String orderStatus,@PageableDefault(page = 0, size = 5, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) throws NotFoundException, DataNotFound {
        Page<OrderResponse> orderResponseo = orderService.getOrderResponseByOrderStatus(orderStatus,pageable);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                ConvertPageToPaginationDTO.convertPageToPaginationDTO(orderResponseo)), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllOrder(@PageableDefault(page = 0, size = 5, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) throws DataNotFound, NotFoundException {
        Page<OrderResponse> orderResponse = orderService.getAllOrder(pageable);
        return new ResponseEntity<>(new ResponseWapper<>(
                EHttpStatus.SUCCESS,
                HttpStatus.OK.name(),
                HttpStatus.OK.value(),
                orderResponse), HttpStatus.OK);}
}
