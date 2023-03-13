package com.accodigi.ecart.service;

import com.accodigi.ecart.model.Product;
import com.accodigi.ecart.model.User;
import com.accodigi.ecart.model.cart.Cart;
import com.accodigi.ecart.model.cart.CartItem;
import com.accodigi.ecart.model.order.Order;
import com.accodigi.ecart.model.order.OrderItem;
import com.accodigi.ecart.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    final String PAYMENT_REF_ID = "1234";
    OrderService orderService;
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private SupplierService supplierService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(supplierService, orderRepository);
    }

    @Test
    void testCreateOrder() {
        Cart cart = new Cart();
        User user = new User();
        user.setId(1L);
        cart.setUser(user);

        Product product1 = new Product();
        product1.setProductId(1L);

        CartItem cartItem1 = new CartItem();
        cartItem1.setProduct(product1);
        cartItem1.setQuantity(2L);

        Set<CartItem> cartItems = Set.of(cartItem1);
        cart.setCartItems(cartItems);

        Order order = new Order();
        order.setUser(user);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setProduct(product1);
        orderItem1.setQuantity(2L);

        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(orderItem1);
        order.setOrderItems(orderItems);

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(supplierService.notifySupplier(any(Order.class))).thenReturn(Mono.just(order));

        Order createdOrder = orderService.createOrder(cart, PAYMENT_REF_ID);
        Mockito.verify(orderRepository).save(argThat(o -> {
            assertEquals(user, o.getUser());
            assertEquals(PAYMENT_REF_ID, o.getPaymentReferenceId());
            assertEquals(orderItems, o.getOrderItems());
            return true;
        }));
        assertEquals(order, createdOrder);
    }

    @Test
    public void testConvertCartToOrderItems() {
        Product product1 = new Product();
        product1.setProductId(1L);

        CartItem cartItem1 = new CartItem();
        cartItem1.setProduct(product1);
        cartItem1.setQuantity(2L);

        Product product2 = new Product();
        product2.setProductId(2L);
        CartItem cartItem2 = new CartItem();
        cartItem2.setProduct(product2);
        cartItem2.setQuantity(3L);

        Set<CartItem> cartItems = Set.of(cartItem1, cartItem2);
        Set<OrderItem> orderItems = orderService.convertCartToOrderItems(cartItems);

        assertEquals(2, orderItems.size());
        assertTrue(orderItems.stream().anyMatch(item -> {
            assertEquals(product1, item.getProduct());
            assertEquals(2L, item.getQuantity().longValue());
            return true;
        }));
    }
}