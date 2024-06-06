package com.ra.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompositeKey implements Serializable {
    @Column(name = "orders_id")
    private Long orderId;
    @Column(name= "productDetail_id")
    private Long productDetailId;
}
