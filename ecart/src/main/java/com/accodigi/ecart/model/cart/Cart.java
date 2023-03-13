package com.accodigi.ecart.model.cart;

import com.accodigi.ecart.model.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @OneToMany(cascade = {CascadeType.ALL})
    private Set<CartItem> cartItems;

}