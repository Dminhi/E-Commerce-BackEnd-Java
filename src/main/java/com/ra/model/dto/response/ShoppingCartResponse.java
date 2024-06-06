package com.ra.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShoppingCartResponse {
    private long id;
    private int orderQuantity;
    private String productDetailName;
    private Double productPrice;
    private String productCategory;
}
