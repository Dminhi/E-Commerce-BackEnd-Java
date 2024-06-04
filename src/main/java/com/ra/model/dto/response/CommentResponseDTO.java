package com.ra.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.model.entity.Comment;
import com.ra.model.entity.Product;
import com.ra.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentResponseDTO {
    private Long id;
    private String review;
    private String product;
    private double rating;
    private String user;
    private boolean status;

    public CommentResponseDTO(Comment comment) {
        this.id = comment.getId();
        this.review = comment.getReview();
        this.product = comment.getProduct().getProductName();
        this.rating = comment.getRating();
        this.user = comment.getUser().getUsername();
        this.status = comment.isStatus();
    }
}
