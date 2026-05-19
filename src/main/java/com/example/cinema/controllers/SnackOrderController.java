package com.example.cinema.controllers;

import com.example.cinema.models.SnackOrder;
import com.example.cinema.services.SnackOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST API for snack checkout — requires a valid user id (authenticated client).
 */
@RestController
@RequestMapping("/api/orders")
public class SnackOrderController {

    @Autowired
    private SnackOrderService snackOrderService;

    /**
     * POST /api/orders
     * Body: { "userId": 1, "items": [ { "snackId": "S001", "quantity": 2 } ] }
     */
    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody Map<String, Object> body) {
        Object userIdObj = body.get("userId");
        if (userIdObj == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Authentication required. Please log in."));
        }

        Long userId;
        try {
            userId = Long.valueOf(String.valueOf(userIdObj));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid user session. Please log in again."));
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");

        return snackOrderService.placeOrder(userId, items)
                .map(order -> ResponseEntity.ok(Map.of(
                        "message", "Order placed successfully",
                        "orderId", order.getOrderId(),
                        "username", order.getUsername(),
                        "totalAmount", order.getTotalAmount()
                )))
                .orElse(ResponseEntity.badRequest().body(Map.of(
                        "error", "Unable to place order. Check your cart and login, then try again."
                )));
    }
}
