package com.ra.model.dto.request;
import lombok.*;
import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentRequest {
    private Long id;
    private String comment;
    private Long productDetailId;
    private Long userId;
    private boolean status;
    private LocalDateTime createdAt;
}
