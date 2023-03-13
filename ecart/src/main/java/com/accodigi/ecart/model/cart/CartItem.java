package com.accodigi.ecart.model.cart;

import com.accodigi.ecart.model.Product;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Data
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @Min(1)
    private Long quantity;

    public int hashCode() {
        return getProduct().getProductId().intValue();
    }

    public boolean equals(CartItem another) {
        if (another == null) {
            return false;
        }
        return this.product.getProductId().intValue() == (another).getProduct().getProductId();
    }
}
