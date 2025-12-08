package com.kentwardrobe.backend.controller;

import com.kentwardrobe.backend.model.Order;
import com.kentwardrobe.backend.model.OrderItem;
import com.kentwardrobe.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map; // Add this import

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000") // Allow React to talk to this
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    // POST: Create a new order (Checkout)
    @PostMapping("/place")
    public Order placeOrder(@RequestBody Order order) {
        // 1. Set the date/time automatically
        order.setCreatedAt(LocalDateTime.now());

        // 2. Set the status automatically
        order.setStatus("Processing");

        // 3. Link the items to the order (Crucial step!)
        // Because JSON comes in as nested items, we need to tell each Item "You belong to THIS order"
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                item.setOrder(order);
            }
        }

        // 4. Save to Database (Cascades to order_items too)
        return orderRepository.save(order);
    }

    // GET: Get orders by email (For Transaction History)
    @GetMapping("/{email}")
    public List<Order> getOrdersByEmail(@PathVariable String email) {
        return orderRepository.findByEmail(email);
    }

    // ADMIN: Get ALL orders (Sorted by newest)
    @GetMapping("/all")
    public List<Order> getAllOrders() {
        // We use built-in findAll, but you might want to sort it in Java or Repository
        return orderRepository.findAll();
    }

    // ADMIN: Update Order Status
    @PutMapping("/status/{id}")
    public Order updateStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String newStatus = payload.get("status");
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(newStatus);
                    return orderRepository.save(order);
                })
                .orElse(null);
    }
}