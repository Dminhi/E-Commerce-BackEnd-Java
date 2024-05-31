package com.ra.model.dto.response;

import com.ra.model.entity.Config;
import com.ra.model.entity.Product;
import com.ra.model.entity.ProductDetail;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDetailResponseDTO {
    private Long id;
    private String productDetailName;
    private double unitPrice;
    private String image;
    private int stock;
    private String color;
    private Set<String> configs;
    private String product;
    private boolean status;
    public ProductDetailResponseDTO(ProductDetail productDetail) {
        this.id = productDetail.getId();
        this.productDetailName=productDetail.getProductDetailName();
        this.unitPrice = productDetail.getUnitPrice();
        this.stock = productDetail.getStock();
        this.image = productDetail.getImage();
        this.color = productDetail.getColor().getColorName();
        this.configs = productDetail.getConfigs().stream()
                .map(Config::getConfigName)
                .collect(Collectors.toSet());
        this.product = productDetail.getProduct().getProductName();
        this.status = productDetail.isStatus();
    }
}
