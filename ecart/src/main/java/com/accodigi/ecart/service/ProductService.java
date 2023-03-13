package com.accodigi.ecart.service;


import com.accodigi.ecart.exception.ProductNotFoundException;
import com.accodigi.ecart.model.Product;
import com.accodigi.ecart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product getProduct(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return product.get();
        }

        throw new ProductNotFoundException();
    }
}
