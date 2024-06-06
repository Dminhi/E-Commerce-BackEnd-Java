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
@Entity
@Builder
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String brandName;
    private String description;
    private LocalDate createdAt = LocalDate.now();
    private String image;
    private boolean status = true;
    @OneToMany(mappedBy = "brand" ,fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Product> products;
}
