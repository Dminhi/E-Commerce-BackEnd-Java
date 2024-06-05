package com.ra.service.wishlist;

import com.ra.exception.AccountLockedException;
import com.ra.exception.NotFoundException;
import com.ra.model.dto.request.WishListRequest;
import com.ra.model.dto.response.WishListResponse;
import com.ra.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IWishListService {
    String save(WishListRequest wishListRequest) throws NotFoundException;
    Page<WishListResponse> findAll(Pageable pageable);
    void deleteWishListById(Long id) throws NotFoundException, AccountLockedException;

    List<Long> findProductIdByUserId(Long id);

}
