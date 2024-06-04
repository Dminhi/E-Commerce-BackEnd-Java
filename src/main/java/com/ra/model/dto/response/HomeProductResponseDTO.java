package com.ra.model.dto.response;

import com.ra.model.entity.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
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
    private List<Image> imageList;
    private List<ProductDetailResponseDTO> productDetails;
    private List<CommentResponseDTO> comments;

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
        this.imageList = product.getImages();
        this.productDetails = product.getProductDetails().stream()
                .map(ProductDetailResponseDTO::new)
                .collect(Collectors.toList());
        this.comments = product.getComments().stream()
                .map(CommentResponseDTO::new)
                .collect(Collectors.toList());
    }
}