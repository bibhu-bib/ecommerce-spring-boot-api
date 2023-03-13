package com.accodigi.ecart.repository;

import com.accodigi.ecart.model.User;
import com.accodigi.ecart.model.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<Cart, Long> {
    //TODO
    List<Cart> findAllByUser(User user);


    Optional<Cart> findOneByUser(User user);
}
