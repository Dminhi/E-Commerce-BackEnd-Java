package com.ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double unitPrice;
    private String image;
    private String productDetailName;
    private int stock;
    @ManyToOne
    @JoinColumn(name = "color_id", referencedColumnName = "id")
    private Color color;

    @OneToMany(mappedBy = "productDetail", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Config> configs = new HashSet<>();

    private boolean status;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @OneToMany(mappedBy = "productDetail",fetch = FetchType.EAGER )
    @JsonIgnore
    private List<Review> feedbacks;

    @OneToMany(mappedBy = "productDetail", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<OrderDetail> orderDetail;

    @OneToMany(mappedBy = "productDetail", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Comment> comments;
}