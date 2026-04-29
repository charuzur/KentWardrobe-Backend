package com.kentwardrobe.backend.controller;

import com.kentwardrobe.backend.model.Product;
import com.kentwardrobe.backend.model.User;
import com.kentwardrobe.backend.model.Wishlist;
import com.kentwardrobe.backend.repository.ProductRepository;
import com.kentwardrobe.backend.repository.UserRepository;
import com.kentwardrobe.backend.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "http://localhost:3000")
public class WishlistController {

    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    // GET: Get all products in user's wishlist
    @GetMapping("/{userId}")
    public ResponseEntity<List<Product>> getUserWishlist(@PathVariable Long userId) {
        List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);

        // Use Streams to safely extract products
        List<Product> products = wishlists.stream()
                .map(Wishlist::getProduct)
                .collect(Collectors.toList());

        return ResponseEntity.ok(products);
    }

    // POST: Toggle (Add/Remove)
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, String>> toggleWishlist(@RequestBody Map<String, Long> payload) {
        Long userId = payload.get("userId");
        Long productId = payload.get("productId");

        // Defensive Check: Prevent null IDs from creating ghost rows
        if (userId == null || productId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid IDs provided"));
        }

        Optional<Wishlist> existing = wishlistRepository.findByUserIdAndProductId(userId, productId);

        if (existing.isPresent()) {
            wishlistRepository.delete(existing.get());
            return ResponseEntity.ok(Map.of("message", "Removed"));
        } else {
            // Find the actual entities to ensure they exist in the DB
            User user = userRepository.findById(userId).orElse(null);
            Product product = productRepository.findById(productId).orElse(null);

            if (user != null && product != null) {
                Wishlist w = new Wishlist();
                w.setUser(user);
                w.setProduct(product);
                wishlistRepository.save(w);
                return ResponseEntity.ok(Map.of("message", "Added"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "User or Product not found"));
            }
        }
    }
}