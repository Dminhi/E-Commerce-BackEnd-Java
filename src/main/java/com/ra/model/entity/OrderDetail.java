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
public class OrderDetail {
    @EmbeddedId
    private CompositeKey id;
    private String orderDetailName;
    @ManyToOne
    @JoinColumn(name = "product", referencedColumnName = "id")
    private ProductDetail productDetail;
    private double unitPrice;
    private int oderQuantity;
    @ManyToOne
    @JoinColumn(name = "oders", referencedColumnName = "id")
    private Orders orders;
}
