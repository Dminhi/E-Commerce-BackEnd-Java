package com.ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serialNumber;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private double totalPrice;
    private double totalPriceAfterCoupons;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String note;
    private String province;
    private String streetAddress;
    private String phone;
    private String receiveName;
    private String ward;
    private String district;
    private LocalDate createdAt;
    private LocalDate receiveAt;
    private double totalDiscountedPrice;
    @OneToMany(mappedBy = "orders", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<OrderDetail> orderDetail;
      @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coupons_id", referencedColumnName = "id")
    private Coupons coupons;
}
