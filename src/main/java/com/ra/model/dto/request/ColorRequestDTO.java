package com.ra.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ColorRequestDTO {
    private Long id;
    @NotEmpty(message = "colorName Name cannot be empty")
    private String colorName;
    private boolean status = true;
}
