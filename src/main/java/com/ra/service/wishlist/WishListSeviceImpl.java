package com.ra.service.wishlist;


import com.ra.exception.AccountLockedException;
import com.ra.exception.NotFoundException;
import com.ra.model.dto.request.WishListRequest;
import com.ra.model.dto.response.WishListResponse;
import com.ra.model.entity.WishList;
import com.ra.repository.IProductRepository;
import com.ra.repository.IUserRepository;
import com.ra.repository.IWishListRepository;
import com.ra.security.principle.UserDetailsCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WishListSeviceImpl implements IWishListService{
    @Autowired
    private IWishListRepository wishListRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IUserRepository userRepository;

    @Override
    public String save(WishListRequest wishListRequest) throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authentication.getPrincipal();
        if(wishListRepository.existsByProductIdAndUserId(wishListRequest.getProductId(), userDetailsCustom.getId())){
            wishListRepository.deleteWishListByProductIdAndUserId(wishListRequest.getProductId(), userDetailsCustom.getId());
            return "unlike successfully";
        }
        WishList wishList = WishList.builder()
                .createdAt(LocalDate.now())
                .product(productRepository.findById(wishListRequest.getProductId()).orElseThrow(()->new NotFoundException("product not found")))
                .user(userRepository.findById(userDetailsCustom.getId()).orElseThrow(()-> new NotFoundException("user not found")))
                .build();
        wishListRepository.save(wishList);
        return "like successfully";
    }

    @Override
    public Page<WishListResponse> findAll(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authentication.getPrincipal();

        Page<WishList> wishLists = wishListRepository.findAllByUserId(userDetailsCustom.getId(), pageable);

        return wishLists.map(wishList -> WishListResponse.builder()
                .product(wishList.getProduct())
                .build());
    }

    @Override
    public void deleteWishListById(Long id) throws NotFoundException, AccountLockedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authentication.getPrincipal();
        WishList wishList = wishListRepository.findById(id).orElseThrow(()->new NotFoundException("wishlist not found"));
        if (!wishList.getUser().getId().equals(userDetailsCustom.getId()) ){
            throw new AccountLockedException("Unauthorized");
        }
        wishListRepository.delete(wishList);
    }

    @Override
    public List<Long> findProductIdByUserId(Long id) {
        return wishListRepository.getAllProductIdByUserId(id);
    }
}
