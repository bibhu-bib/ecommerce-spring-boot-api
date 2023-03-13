package com.accodigi.ecart.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "payment_info")
public class PaymentInfo {

    @ManyToOne(cascade = {CascadeType.ALL})
    User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentMode;

    private String walletId;

}
