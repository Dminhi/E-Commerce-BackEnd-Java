package com.ra.model.dto.response;

import com.ra.model.entity.Image;
import com.ra.model.entity.Product;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductResponseDTO {
    private Long id;
    private String sku;
    private String productName;
    private String description;
    private String image;
    private String category;
    private String brand;
    private boolean status;
    private LocalDate createdAt;
    public ProductResponseDTO(Product product) {
        this.id = product.getId();
        this.sku = product.getSku();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.image = product.getImage();
        this.category = product.getCategory().getCategoryName();
        this.brand = product.getBrand().getBrandName();
        this.status = product.isStatus();
        this.createdAt = product.getCreatedAt();
    }
}
