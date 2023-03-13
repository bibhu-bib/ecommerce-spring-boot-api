package com.accodigi.ecart.dto;

import lombok.Data;

@Data
public class OrderItem {
    private Long id;
    private Long quantity;
    private Product product;

}
