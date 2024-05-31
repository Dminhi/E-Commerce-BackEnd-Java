package com.ra.service.wishlist;

import com.ra.exception.AccountLockedException;
import com.ra.exception.NotFoundException;
import com.ra.model.dto.request.WishListRequest;
import com.ra.model.dto.response.WishListResponse;

import java.util.List;

public interface IWishListService {
    String save(WishListRequest wishListRequest) throws NotFoundException;
    List<WishListResponse> findAll();
    void deleteWishListById(Long id) throws NotFoundException, AccountLockedException;
}
