package com.accodigi.ecart.dto;

import lombok.Data;

@Data
public class Product {
    private Long productId;
    private int supplierId;
    private Long stockCount;
    private double price;
    private String description;
    private String title;
    private String category;
    private String brand;

}
