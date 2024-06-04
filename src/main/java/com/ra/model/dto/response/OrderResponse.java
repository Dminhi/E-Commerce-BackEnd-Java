package com.ra.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ra.model.entity.Status;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {
    private Double totalPrice;
    private String serialNumber;
    private Status orderStatus;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdAt;
    private List<OrderDetailDTO> orderDetail;
}
