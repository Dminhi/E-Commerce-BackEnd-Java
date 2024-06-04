package com.ra.model.dto.request;

import com.ra.model.entity.Category;
import com.ra.model.entity.Color;
import com.ra.model.entity.Config;
import com.ra.model.entity.Image;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductRequestDTO {
    private Long id;
    private String sku;
    @NotEmpty(message = "productName Name cannot be empty")
    private String productName;
    @NotEmpty(message = "Description Name cannot be empty")
    private String description;
    private MultipartFile image;
    @NotNull(message = "Category is mandatory!")
    private Long categoryId;
    private Long brandId;
    private Set<MultipartFile> imageSet;
    private boolean status=true;
    private LocalDate CreatedAt = LocalDate.now();
}
