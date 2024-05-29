package com.ra.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.model.entity.Brand;
import com.ra.model.entity.Category;
import com.ra.model.entity.Product;
import lombok.*;

import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BrandResponseDTO {
    private Long id;
    private String brandName;
    private String description;
    private String image;
    private boolean status;
    @JsonIgnore
    private Set<Product> products;
    public BrandResponseDTO(Brand brand) {
        this.id = brand.getId();
        this.brandName = brand.getBrandName();
        this.description = brand.getDescription();
        this.image = brand.getImage();
        this.status = brand.isStatus();
        this.products = brand.getProducts();

    }
}
