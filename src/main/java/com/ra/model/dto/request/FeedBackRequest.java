package com.ra.model.dto.request;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FeedBackRequest {
    private int rating;
    private String feedback;
    private LocalDate createdAt;
    private boolean status;
    private Long orderId ;
}
