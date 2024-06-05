package com.ra.model.dto.response;

import com.ra.model.entity.Comment;
import com.ra.model.entity.CommentDetail;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentDetailResponseDTO {
    private Long id;
    private String reviewParent;
    private String review;
    private double rating;
    private String user;
    private boolean status;
    public CommentDetailResponseDTO(CommentDetail comment) {
        this.id = comment.getId();
        this.reviewParent = comment.getComment().getComment();
        this.review = comment.getReview();
        this.user = comment.getUser().getUsername();
        this.status = comment.isStatus();
    }
}
