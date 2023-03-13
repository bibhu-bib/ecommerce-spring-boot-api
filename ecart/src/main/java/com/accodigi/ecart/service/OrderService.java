package com.accodigi.ecart.service;


import com.accodigi.ecart.model.cart.Cart;
import com.accodigi.ecart.model.cart.CartItem;
import com.accodigi.ecart.model.order.Order;
import com.accodigi.ecart.model.order.OrderItem;
import com.accodigi.ecart.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final SupplierService supplierService;
    private final OrderRepository orderRepository;

    public Order createOrder(Cart cart, String paymentId) {
        final Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderItems(convertCartToOrderItems(cart.getCartItems()));
        order.setPaymentReferenceId(paymentId);

        Order savedOrder = orderRepository.save(order);
        Mono<Order> result = supplierService.notifySupplier(savedOrder);
        result.subscribe(f -> log.info("Received fulfillment notification"));

        return savedOrder;
    }

    protected Set<OrderItem> convertCartToOrderItems(Set<CartItem> cartItems) {
        return cartItems.stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(item.getQuantity());
            orderItem.setProduct(item.getProduct());
            return orderItem;
        }).collect(Collectors.toSet());
    }
}
