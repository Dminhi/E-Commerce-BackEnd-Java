package com.ra.model.dto.request;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SearchCriteria {
    private String brand;
    private String category;
    private Double minPrice;
    private Double maxPrice;
}
