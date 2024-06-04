package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int rating;
    private String comments;
    private LocalDate createdAt;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean status;
    @ManyToOne
    @JoinColumn(name = "productDetail_id", nullable = false)
    private ProductDetail productDetail;
}
