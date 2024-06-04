package com.ra.model.dto.request;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReviewRequest {
    private int rating;
    private String comments;
    private LocalDate createdAt;
    private boolean status;
    private Long productDetailId ;
}
