package com.ra.model.dto.request;
import lombok.*;
import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentDetailRequestDTO {
    private Long id;
    private Long parentId;
    private String review;
    private Long productId;
    private Long userId;
    private boolean status;
    private LocalDateTime createdAt;
}
