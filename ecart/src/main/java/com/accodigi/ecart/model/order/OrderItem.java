package com.accodigi.ecart.model.order;

import com.accodigi.ecart.model.Product;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long quantity;
    @ManyToOne
    private Product product;

}
