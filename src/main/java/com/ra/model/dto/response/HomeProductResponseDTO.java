package com.ra.model.dto.response;

import com.ra.model.entity.Config;
import com.ra.model.entity.Product;
import com.ra.model.entity.ProductDetail;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HomeProductResponseDTO {
    private Long id;
    private String sku;
    private String productName;
    private String description;
    private String image;
    private String category;
    private String brand;
    private boolean status;
    private LocalDate createdAt;

    private List<ProductDetail> productDetailResponseDTOSet;
    private List<Config> configList;
    public HomeProductResponseDTO(Product product) {
        this.id = product.getId();
        this.sku = product.getSku();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.image = product.getImage();
        this.category = product.getCategory().getCategoryName();
        this.brand = product.getBrand().getBrandName();
        this.status = product.isStatus();
        this.createdAt = product.getCreatedAt();

        this.productDetailResponseDTOSet = product.getProductDetails();

        // Chuyển đổi danh sách ProductDetail thành danh sách Config
        this.configList = product.getProductDetails().stream()
                .map(ProductDetail::getConfigs)
                .flatMap(Set::stream)
                .collect(Collectors.toList());
    }

}
