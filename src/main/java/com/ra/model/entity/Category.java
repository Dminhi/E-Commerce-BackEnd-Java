package com.ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String categoryName;
    private String description;
    private String image;
    private boolean status = true;
    private LocalDate createdAt = LocalDate.now();
    @OneToMany(mappedBy = "category" ,fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Product> products;

}
