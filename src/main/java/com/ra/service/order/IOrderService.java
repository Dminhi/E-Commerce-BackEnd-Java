package com.ra.service.order;

import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.model.dto.response.OrderResponse;
import com.ra.model.entity.Orders;
import com.ra.model.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService{
    Page<OrderResponse> getOrdersByUserId(Pageable pageable) throws DataNotFound;
    OrderResponse getOrderResponseBySerialNumber(String serialNumber) throws NotFoundException, DataNotFound;

    Page<OrderResponse> getOrderResponseByOrderStatus(String orderStatus, Pageable pageable) throws NotFoundException, DataNotFound;

    OrderResponse getOrderByOrderId(Long id) throws NotFoundException, DataNotFound;

    OrderResponse updateOrderStatus(Long id, Status status) throws NotFoundException;

    Page<OrderResponse> getAllOrder(Pageable pageable) throws NotFoundException;
    Page<OrderResponse> getOrderResponseByOrderStatusByUser(String status, Pageable pageable) throws NotFoundException;





}
