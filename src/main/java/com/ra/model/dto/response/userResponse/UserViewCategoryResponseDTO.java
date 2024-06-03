package com.ra.model.dto.response.userResponse;

import com.ra.model.entity.Category;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserViewCategoryResponseDTO {
    private String categoryName;
    private String image;

    public UserViewCategoryResponseDTO(Category category) {
        this.categoryName = category.getCategoryName();
        this.image = category.getImage();

    }
}
