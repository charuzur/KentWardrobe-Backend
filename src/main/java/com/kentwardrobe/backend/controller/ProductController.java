package com.kentwardrobe.backend.controller;

import com.kentwardrobe.backend.model.Product;
import com.kentwardrobe.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // GET all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // POST a new product
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // --- NEW: UPDATE PRODUCT ---
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setPrice(productDetails.getPrice());
                    product.setCategory(productDetails.getCategory());
                    product.setBadge(productDetails.getBadge());
                    product.setDescription(productDetails.getDescription());
                    product.setImg(productDetails.getImg());
                    product.setType(productDetails.getType());
                    return productRepository.save(product);
                })
                .orElse(null);
    }

    // --- NEW: DELETE PRODUCT ---
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}