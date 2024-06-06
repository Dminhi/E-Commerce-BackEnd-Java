package com.ra.model.dto.request.shoppingcart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShoppingCartEditRequest {
    @NotNull
    @Min(value = 1,message = "orderQuantity must be more than 0")
    private int orderQuantity;
}
