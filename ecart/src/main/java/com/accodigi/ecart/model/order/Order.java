package com.accodigi.ecart.model.order;

import com.accodigi.ecart.model.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;


@Entity
@Data
@Table(name = "orders")
public class Order {
    @OneToOne
    User user;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = {CascadeType.ALL})
    private Set<OrderItem> orderItems;

    private String paymentReferenceId;
}
