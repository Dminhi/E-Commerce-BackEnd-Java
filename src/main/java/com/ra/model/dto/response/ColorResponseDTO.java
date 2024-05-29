package com.ra.model.dto.response;

import com.ra.model.entity.Color;
import com.ra.model.entity.Config;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ColorResponseDTO {
    private Long id;
    private String colorName;

    private boolean status ;

    public ColorResponseDTO(Color color) {
        this.id = color.getId();
        this.colorName = color.getColorName();
        this.status = color.isStatus();
    }
}
