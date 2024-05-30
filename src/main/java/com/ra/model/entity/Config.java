package com.ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String configName;
    private String configValue;
    private boolean status = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "productDetail_id", referencedColumnName = "id")
    @JsonIgnore
    private ProductDetail productDetail;

}