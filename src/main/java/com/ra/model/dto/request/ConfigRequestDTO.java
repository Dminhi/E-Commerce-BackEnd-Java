package com.ra.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.model.entity.ProductDetail;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ConfigRequestDTO {

    private Long id;
    @NotEmpty(message = "configName Name cannot be empty")
    private String configName;
    @NotEmpty(message = "configValue cannot be empty")
    private String configValue;
    private boolean status = true;

}
