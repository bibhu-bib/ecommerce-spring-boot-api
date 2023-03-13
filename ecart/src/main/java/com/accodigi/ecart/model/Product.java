package com.accodigi.ecart.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private int supplierId;
    private Long stockCount;
    private double price;
    private String description;
    private String title;
    private String category;
    private String brand;

}
