package com.ra.model.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductEditRequestDTO {
    private Long id;
    private String sku;
    private String productName;
    private String description;
    private MultipartFile image;

    private Long categoryId;
    private Long brandId;
    private Set<MultipartFile> imageSet;
    private LocalDate updatedAt = LocalDate.now();
}
