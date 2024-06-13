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
    private String comment;
    @ManyToOne
    @JoinColumn(name = "productDetail_id")
    @JsonIgnore
    private ProductDetail productDetail;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private boolean status;
    private LocalDateTime createdAt;
    private boolean commentStatus;

//    @OneToMany(mappedBy = "comment" ,fetch = FetchType.EAGER)
//    @JsonIgnore
//    private Set<CommentDetail> commentDetails;
}
