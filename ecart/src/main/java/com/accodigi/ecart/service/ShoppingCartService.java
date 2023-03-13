package com.accodigi.ecart.service;

import com.accodigi.ecart.dto.AddToCartRequest;
import com.accodigi.ecart.dto.CheckOutRequest;
import com.accodigi.ecart.dto.Item;
import com.accodigi.ecart.exception.EmptyCartException;
import com.accodigi.ecart.exception.InvalidPaymentModeException;
import com.accodigi.ecart.exception.ProductNotFoundException;
import com.accodigi.ecart.exception.UserNotFoundException;
import com.accodigi.ecart.model.PaymentInfo;
import com.accodigi.ecart.model.Product;
import com.accodigi.ecart.model.User;
import com.accodigi.ecart.model.cart.Cart;
import com.accodigi.ecart.model.cart.CartItem;
import com.accodigi.ecart.model.order.Order;
import com.accodigi.ecart.repository.ShoppingCartRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ShoppingCartService {
    private UserService userService;
    private PaymentService paymentService;
    private ProductService productService;
    private OrderService orderService;
    private ShoppingCartRepository cartRepository;

    public Cart addCartItem(AddToCartRequest cartRequest) throws UserNotFoundException {
        User user = userService.getUser(cartRequest.getUserId());

        List<Item> items = cartRequest.getProducts();
        if (CollectionUtils.isEmpty(items)) {
            throw new ProductNotFoundException();
        }

        Cart cart;
        Optional<Cart> existingCart = cartRepository.findOneByUser(user);
        if (existingCart.isEmpty()) {
            cart = new Cart();
            cart.setUser(user);
            Set<CartItem> cartItems = items.stream()
                    .map(this::convertToCartItem)
                    .collect(Collectors.toSet());
            cart.setCartItems(cartItems);
        } else {
            cart = existingCart.get();
            Set<CartItem> cartItems = cart.getCartItems();

            for (Item item : items) {
                Long productId = item.getProductId();
                CartItem cartItem = cartItems.stream()
                        .filter(f -> Objects.equals(f.getProduct().getProductId(), productId))
                        .findFirst().orElse(null);
                if (cartItem == null) {
                    cartItems.add(convertToCartItem(item));
                } else {
                    cartItems.remove(cartItem);
                    cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                    cartItems.add(cartItem);
                }
            }
            cart.getCartItems().addAll(cartItems);
        }

        return cartRepository.save(cart);
    }

    @Transactional
    public Order checkout(CheckOutRequest request) throws UserNotFoundException, EmptyCartException {
        final User user = userService.getUser(request.getUserId());

        final Optional<Cart> cart = cartRepository.findOneByUser(user);
        if (cart.isEmpty() || CollectionUtils.isEmpty(cart.get().getCartItems())) {
            throw new EmptyCartException("Shopping cart is empty");
        }

        Optional<PaymentInfo> paymentInfo = paymentService.fetchPaymentInfo(user, request.getPaymentMode(), request.getWalletId());
        if (paymentInfo.isEmpty()) {
            throw new InvalidPaymentModeException("Invalid payment mode");
        }

        return processOrder(paymentInfo.get(), cart.get());
    }

    @Transactional
    public Order processOrder(PaymentInfo paymentInfo, Cart cart) {
        String paymentId = paymentService.processPayment(paymentInfo);
        Order order = orderService.createOrder(cart, paymentId);
        cartRepository.delete(cart);
        return order;
    }

    private CartItem convertToCartItem(Item item) {
        Product product = productService.getProduct(item.getProductId());
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(item.getQuantity());
        cartItem.setProduct(product);
        return cartItem;
    }
}
