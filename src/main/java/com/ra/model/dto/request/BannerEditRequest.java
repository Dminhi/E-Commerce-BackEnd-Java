package com.ra.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BannerEditRequest {
    private Long id;
    @NotEmpty(message = "Banner Name cannot be empty")
    private String bannerName;
    @NotEmpty(message = "description cannot be empty")
    private String description;
    private MultipartFile file;
    private LocalDate createdAt = LocalDate.now();
    private boolean status = true;
}
