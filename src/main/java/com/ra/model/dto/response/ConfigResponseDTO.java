package com.ra.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.model.entity.Category;
import com.ra.model.entity.Config;
import com.ra.model.entity.ProductDetail;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ConfigResponseDTO {

    private Long id;
    private String configName;
    private String configValue;
    private boolean status ;

    public ConfigResponseDTO(Config config) {
        this.id = config.getId();
        this.configName = config.getConfigName();
        this.configValue = config.getConfigValue();
        this.status = config.isStatus();
    }
}
