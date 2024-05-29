package com.ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Coupons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String code;
    private String discount;
    private LocalDate startDate;
    private LocalDate endDate;
    private int quantity;
    private boolean status = true;
    @OneToMany(mappedBy = "coupons")
    @JsonIgnore
    private Set<Orders> orders;
}
