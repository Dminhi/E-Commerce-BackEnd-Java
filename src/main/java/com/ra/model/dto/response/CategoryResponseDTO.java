package com.ra.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.model.entity.Category;
import com.ra.model.entity.Product;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CategoryResponseDTO {
    private Long id;
    private String categoryName;
    private String description;
    private String image;
    private boolean status;
    private LocalDate createdAt;
    @JsonIgnore
    private Set<Product> products;
    public CategoryResponseDTO(Category category) {
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
        this.description = category.getDescription();
        this.image = category.getImage();
        this.status = category.isStatus();
        this.createdAt = category.getCreatedAt();
        this.products = category.getProducts();

    } }
