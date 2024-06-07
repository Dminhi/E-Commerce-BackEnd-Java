package com.ra.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailDTO {
    private String userName;
    private String productName;
    private int quantity;
    private double price;
    private double totalPrice;
    private String image;
}
