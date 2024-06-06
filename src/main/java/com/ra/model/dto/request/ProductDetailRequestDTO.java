package com.ra.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.model.entity.Color;
import com.ra.model.entity.Config;
import com.ra.model.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDetailRequestDTO {
    private Long id;
    private String productDetailName;
    private double unitPrice;
    private MultipartFile image;
    private int stock;
    private Long colorId;
    private Long productId;
    private boolean status = true;
    private Set<ConfigRequestDTO> configs;
}
