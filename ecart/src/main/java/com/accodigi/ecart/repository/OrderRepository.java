package com.accodigi.ecart.repository;

import com.accodigi.ecart.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
