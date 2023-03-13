package com.accodigi.ecart.service;

import com.accodigi.ecart.exception.ProductNotFoundException;
import com.accodigi.ecart.model.Product;
import com.accodigi.ecart.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    final Long TEST_PRODUCT_ID = 1L;
    ProductService service;
    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        service = new ProductService(productRepository);
    }

    @Test
    void getProduct() {

        Product product = new Product();
        product.setProductId(1L);
        product.setTitle("iPhone 9");
        product.setDescription("An apple mobile which is nothing like apple");
        product.setCategory("smartphones");
        product.setPrice(500);
        product.setStockCount(30L);
        product.setSupplierId(6);
        product.setBrand("Apple");

        when(productRepository.findById(TEST_PRODUCT_ID))
                .thenReturn(Optional.of(product));

        final Product productDetails = service.getProduct(TEST_PRODUCT_ID);
        assertEquals(productDetails.getBrand(), "Apple");
        assertEquals(1L, productDetails.getProductId());

        Mockito.verify(productRepository, Mockito.times(1))
                .findById(TEST_PRODUCT_ID);
    }

    @Test
    public void testGetProductNotFound() {
        Mockito.when(productRepository.findById(TEST_PRODUCT_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            service.getProduct(TEST_PRODUCT_ID);
        });
        Mockito.verify(productRepository, Mockito.times(1))
                .findById(TEST_PRODUCT_ID);
    }


}