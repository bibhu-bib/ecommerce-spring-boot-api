package com.accodigi.ecart.utils;

import com.accodigi.ecart.model.PaymentInfo;
import com.accodigi.ecart.model.Product;
import com.accodigi.ecart.model.User;
import com.accodigi.ecart.model.cart.Cart;
import com.accodigi.ecart.model.cart.CartItem;

import java.util.HashSet;

public class TestDataGen {

    public static User getUser() {
        User user = new User();
        user.setId(1L);
        user.setAddress("SH44EE");
        user.setFirstName("Rowan");
        user.setLastName("Rex");
        user.setPhoneNumber("9876543223");
        return user;
    }

    public static Product getProductTestData(Long productId) {
        Product product = new Product();

        product.setProductId(productId);
        product.setTitle("iPhone 9");
        product.setDescription("An apple mobile which is nothing like apple");
        product.setCategory("smartphones");
        product.setPrice(500);
        product.setStockCount(30L);
        product.setSupplierId(6);
        product.setBrand("Apple");
        return product;
    }

    public static PaymentInfo paymentInfo() {
        PaymentInfo paymentInfo = new PaymentInfo();

        paymentInfo.setId(9L);
        paymentInfo.setPaymentMode("UPI");
        paymentInfo.setUser(TestDataGen.getUser());
        paymentInfo.setWalletId("1234@natwest");
        return paymentInfo;

    }

    public static Cart getMockCart(Product mockProduct, User mockUser) {
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cartItem.setProduct(mockProduct);
        cartItem.setQuantity(5L);
        cart.setUser(mockUser);

        HashSet<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        cart.setCartItems(cartItems);
        return cart;
    }
}
