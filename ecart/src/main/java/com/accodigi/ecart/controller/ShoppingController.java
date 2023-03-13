package com.accodigi.ecart.controller;

import com.accodigi.ecart.dto.AddToCartRequest;
import com.accodigi.ecart.dto.CheckOutRequest;
import com.accodigi.ecart.exception.EmptyCartException;
import com.accodigi.ecart.exception.UserNotFoundException;
import com.accodigi.ecart.model.cart.Cart;
import com.accodigi.ecart.model.order.Order;
import com.accodigi.ecart.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/1.0/carts")
@RequiredArgsConstructor
public class ShoppingController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public ResponseEntity addItemsToCart(@RequestBody @Valid AddToCartRequest request)
            throws UserNotFoundException {
        Cart result = shoppingCartService.addCartItem(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkOut(@RequestBody CheckOutRequest request)
            throws UserNotFoundException, EmptyCartException {

        Order order = shoppingCartService.checkout(request);
        return ResponseEntity.ok(order);
    }
}
