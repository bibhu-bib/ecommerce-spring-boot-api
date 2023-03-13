package com.accodigi.ecart.repository;

import com.accodigi.ecart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
