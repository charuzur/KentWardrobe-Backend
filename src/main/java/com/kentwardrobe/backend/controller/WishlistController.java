package com.kentwardrobe.backend.controller;

import com.kentwardrobe.backend.model.Product;
import com.kentwardrobe.backend.model.User;
import com.kentwardrobe.backend.model.Wishlist;
import com.kentwardrobe.backend.repository.ProductRepository;
import com.kentwardrobe.backend.repository.UserRepository;
import com.kentwardrobe.backend.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public List<Product> getUserWishlist(@PathVariable Long userId) {
        List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);
        List<Product> products = new ArrayList<>();
        for (Wishlist w : wishlists) {
            products.add(w.getProduct());
        }
        return products;
    }

    // POST: Toggle (Add/Remove)
    @PostMapping("/toggle")
    public String toggleWishlist(@RequestBody Map<String, Long> payload) {
        Long userId = payload.get("userId");
        Long productId = payload.get("productId");

        Optional<Wishlist> existing = wishlistRepository.findByUserIdAndProductId(userId, productId);

        if (existing.isPresent()) {
            wishlistRepository.delete(existing.get());
            return "{\"message\": \"Removed\"}";
        } else {
            User user = userRepository.findById(userId).orElse(null);
            Product product = productRepository.findById(productId).orElse(null);

            if (user != null && product != null) {
                Wishlist w = new Wishlist();
                w.setUser(user);
                w.setProduct(product);
                wishlistRepository.save(w);
                return "{\"message\": \"Added\"}";
            }
        }
        return "{\"message\": \"Error\"}";
    }
}