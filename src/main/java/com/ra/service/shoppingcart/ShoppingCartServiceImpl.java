package com.ra.service.shoppingcart;

import com.ra.exception.DataNotFound;
import com.ra.exception.NotFoundException;
import com.ra.model.dto.request.shoppingcart.CheckOutRequest;
import com.ra.model.dto.request.shoppingcart.ShoppingCartEditRequest;
import com.ra.model.dto.request.shoppingcart.ShoppingCartRequest;
import com.ra.model.dto.response.ShoppingCartResponse;
import com.ra.model.entity.*;
import com.ra.repository.*;
import com.ra.security.principle.UserDetailsCustom;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ShoppingCartServiceImpl implements IShoppingCartService{
    @Autowired
    private IShoppingCartRepository shoppingCartRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IProductDetailRepository productDetailRepository;
    @Autowired
    private IOrderDetailRepository orderDetailRepository;
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private ICouponRepository couponRepository;

    @Override
    public List<ShoppingCartResponse> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authentication.getPrincipal();
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAllByUserId(userDetailsCustom.getId());
        List<ShoppingCartResponse> shoppingCartResponses = new ArrayList<>();
        for (int i = 0; i < shoppingCarts.size(); i++) {
            ShoppingCartResponse shoppingCartResponse = new ShoppingCartResponse();
            shoppingCartResponse.setProductDetailName(shoppingCarts.get(i).getProductDetail().getProductDetailName());
            shoppingCartResponse.setOrderQuantity(shoppingCarts.get(i).getOrderQuantity());
            shoppingCartResponse.setProductPrice(shoppingCarts.get(i).getProductDetail().getUnitPrice());
            shoppingCartResponse.setProductCategory(shoppingCarts.get(i).getProductDetail().getProduct().getCategory().getCategoryName());
            shoppingCartResponses.add(shoppingCartResponse);
        }
        return shoppingCartResponses;
    }

    @Override
    public ShoppingCartResponse save(ShoppingCartRequest shoppingCartRequest) throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authentication.getPrincipal();
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findByProductDetailIdAndUserId(shoppingCartRequest.getProductDetailId(), userDetailsCustom.getId());
        if(optionalShoppingCart.isPresent()){
            ShoppingCart shoppingCart = optionalShoppingCart.get();
            shoppingCart.setOrderQuantity(shoppingCart.getOrderQuantity()+shoppingCartRequest.getOrderQuantity());
            return convertToShoppingCartResponse(shoppingCartRepository.save(shoppingCart));
        }
        else {
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .orderQuantity(shoppingCartRequest.getOrderQuantity())
                .user(userRepository.findById(userDetailsCustom.getId()).orElseThrow(()->new NotFoundException("user not found")))
                .productDetail(productDetailRepository.findById(shoppingCartRequest.getProductDetailId()).orElseThrow(()->new NotFoundException("product not found")))
        .build();
        return convertToShoppingCartResponse(shoppingCartRepository.save(shoppingCart));}
    }

    @Override
    public ShoppingCartResponse save(ShoppingCartEditRequest shoppingCartEditRequest, Long id) throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authentication.getPrincipal();

        ShoppingCart shoppingCart = shoppingCartRepository.findById(id).orElseThrow(()-> new NotFoundException("shopping cart not found"));
        if(!shoppingCart.getUser().getId().equals(userDetailsCustom.getId())){
            throw new NotFoundException("shoppingCart not found");
        }
        shoppingCart.setOrderQuantity(shoppingCartEditRequest.getOrderQuantity());
        return convertToShoppingCartResponse(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public void deleteShoppingCartById(Long id) throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authentication.getPrincipal();
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id).orElseThrow(()->new NotFoundException("shoppingCart not found"));
        if(!shoppingCart.getUser().getId().equals(userDetailsCustom.getId())){
            throw new NotFoundException("shoppingCart not found");
        }
        shoppingCartRepository.deleteById(id);
    }

    @Override
    public void deleteAllShoppingCart() throws DataNotFound {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authentication.getPrincipal();
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAllByUserId(userDetailsCustom.getId());
        if(shoppingCarts.isEmpty()){
            throw new DataNotFound("shopping cart is empty");
        }
        shoppingCartRepository.deleteShoppingCartByUserId(userDetailsCustom.getId());
    }

    @Override
    public List<ShoppingCartResponse> findAllByUserId() throws DataNotFound {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authentication.getPrincipal();
       List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAllByUserId(userDetailsCustom.getId());
       if(shoppingCarts.isEmpty()){
           throw new DataNotFound("shoppingcart is empty");
       }
        return shoppingCarts.stream().map(this::convertToShoppingCartResponse).toList();
    }

    @Override
    @Transactional
    public Orders checkOut(CheckOutRequest checkOutRequest) throws NotFoundException, DataNotFound {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authentication.getPrincipal();
        User user = userRepository.findById(userDetailsCustom.getId()).orElseThrow(()-> new NotFoundException("user not found"));
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAllByUserId(userDetailsCustom.getId());
        Double totalPrice = shoppingCarts.stream().map(item -> item.getProductDetail().getUnitPrice()*item.getOrderQuantity()).reduce( 0.0,(sum, number)->sum+=number);
        String serialNumber = UUID.randomUUID().toString();
        Optional<Coupons> coupons = couponRepository.findById(checkOutRequest.getCouponId());
        if(coupons.isPresent()){
            totalPrice = totalPrice*(1-Double.parseDouble(coupons.get().getDiscount()));
        }
        Orders orders = Orders.builder()
                .phone(checkOutRequest.getReceivePhone())
                .streetAddress(checkOutRequest.getReceiveAddress())
                .receiveName(checkOutRequest.getReceiveName())
                .serialNumber(serialNumber)
                .note(checkOutRequest.getNote())
                .createdAt(LocalDate.now())
                .user(user)
                .coupons(coupons.orElse(null))
                .totalPrice(totalPrice)
                .status(Status.WAITING)
                .build();
        Orders newOrder = orderRepository.save(orders);

        List<OrderDetail> orderDetails = shoppingCarts.stream().map(shoppingCart -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderDetailName(shoppingCart.getProductDetail().getProductDetailName());
            orderDetail.setUnitPrice(shoppingCart.getProductDetail().getUnitPrice());
            orderDetail.setProductDetail(shoppingCart.getProductDetail());
            orderDetail.setOrders(newOrder);
            orderDetail.setOderQuantity(shoppingCart.getOrderQuantity());
            orderDetail.setId(new CompositeKey(newOrder.getId(),shoppingCart.getProductDetail().getId()));
            return orderDetail;
        }).toList();
        orderDetailRepository.saveAll(orderDetails);
        for (ShoppingCart shoppingCart : shoppingCarts) {
           ProductDetail productDetail = shoppingCart.getProductDetail();
            productDetail.setStock(productDetail.getStock()-shoppingCart.getOrderQuantity());
           productDetailRepository.save(productDetail);
           shoppingCartRepository.deleteById(shoppingCart.getId());
        }
        return newOrder;
    }
    public ShoppingCartResponse convertToShoppingCartResponse(ShoppingCart shoppingCart) {
        // Lấy thông tin từ ShoppingCart
        Long id = shoppingCart.getId();
        int orderQuantity = shoppingCart.getOrderQuantity();

        // Lấy thông tin từ sản phẩm trong giỏ hàng
        String productName = shoppingCart.getProductDetail().getProductDetailName();
        Double productPrice = shoppingCart.getProductDetail().getUnitPrice();
        String productCategory = shoppingCart.getProductDetail().getProduct().getCategory().getCategoryName();

        // Tạo và trả về một đối tượng ShoppingCartResponse với dữ liệu thu được
        return new ShoppingCartResponse(
                id,
                orderQuantity,
                productName,
                productPrice,
                productCategory
        );
    }

}
