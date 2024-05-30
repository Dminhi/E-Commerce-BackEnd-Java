package com.ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double unitPrice;
    private String image;
    private int stock;
    @OneToOne
    @JoinColumn(name = "color_id", referencedColumnName = "id")
    private Color color;

    @OneToMany(mappedBy = "productDetail", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Config> configs;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
}