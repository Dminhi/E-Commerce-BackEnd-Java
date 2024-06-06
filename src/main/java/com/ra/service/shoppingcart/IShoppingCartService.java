package com.ra.service.shoppingcart;
import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.model.dto.request.shoppingcart.CheckOutRequest;
import com.ra.model.dto.request.shoppingcart.ShoppingCartEditRequest;
import com.ra.model.dto.request.shoppingcart.ShoppingCartRequest;
import com.ra.model.dto.response.ShoppingCartResponse;
import com.ra.model.entity.Orders;

import java.util.List;

public interface IShoppingCartService{
    List<ShoppingCartResponse> findAll();
    ShoppingCartResponse save(ShoppingCartRequest shoppingCartRequest) throws NotFoundException;

    ShoppingCartResponse save(ShoppingCartEditRequest shoppingCartEditRequest, Long id) throws NotFoundException;

    void deleteShoppingCartById(Long id) throws NotFoundException;
    void deleteAllShoppingCart() throws NotFoundException, DataNotFound;
    Orders checkOut(CheckOutRequest checkOutRequest) throws NotFoundException, DataNotFound;
    List<ShoppingCartResponse> findAllByUserId() throws DataNotFound;
}
