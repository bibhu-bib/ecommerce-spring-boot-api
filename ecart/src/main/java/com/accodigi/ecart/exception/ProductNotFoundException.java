package com.accodigi.ecart.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {
        super("There are no products in the cart");
    }

}
