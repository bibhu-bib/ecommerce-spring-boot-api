package com.accodigi.ecart.dto;

import lombok.Data;

import java.util.Set;


@Data
public class Order {
    User user;
    private Long id;
    private Set<OrderItem> orderItems;

    private String paymentReferenceId;
}
