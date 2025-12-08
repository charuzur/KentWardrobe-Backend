package com.kentwardrobe.backend.repository;

import com.kentwardrobe.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Custom query to find products by category (for your Men/Women tabs)
    List<Product> findByCategory(String category);
}