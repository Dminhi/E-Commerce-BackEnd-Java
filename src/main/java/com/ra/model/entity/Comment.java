package com.ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String review;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;
    private double rating;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private boolean status;
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "comment" ,fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<CommentDetail> commentDetails;
}
