package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @OneToOne
    @JoinColumn(name = "color_id", referencedColumnName = "id")
    private Color color;
    @OneToOne
    @JoinColumn(name = "config_id", referencedColumnName = "id")
    private Config config;
    @ManyToOne
    @JoinColumn(name = "product", referencedColumnName = "id")
    private Product product;

}
