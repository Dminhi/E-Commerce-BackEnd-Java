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
    private String comment;
    private String productDetail;
    private String user;
    private boolean status;

    public CommentResponseDTO(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.productDetail = comment.getProductDetail().getProductDetailName();
        this.user = comment.getUser().getUsername();
        this.status = comment.isStatus();
    }
}

