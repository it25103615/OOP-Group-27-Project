package com.example.cinema.services;

import com.example.cinema.models.OrderLineItem;
import com.example.cinema.models.Snack;
import com.example.cinema.models.SnackOrder;
import com.example.cinema.models.User;
import com.example.cinema.repositories.SnackOrderRepository;
import com.example.cinema.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Business logic for placing and validating snack orders.
 */
// Marks this class as a Service — Spring manages it and allows @Autowired injection
@Service
public class SnackOrderService {

    @Autowired
    private SnackOrderRepository snackOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SnackService snackService;

    /**
     * Places an order for the given user after validating cart items.
     *
     * @param userId authenticated user id from session  //pass values into a query or method parameter.
     * @param lineItems each map: snackId (String), quantity (Number)
     * @return saved order, or empty if validation fails
     */
    public Optional<SnackOrder> placeOrder(Long userId, List<Map<String, Object>> lineItems) {
        // Reject if no userId provided — can't place order without knowing who's ordering
        if (userId == null) {
            // User not found — return empty to signal failure
            return Optional.empty();
        }

        //Check if the user actually exists in the database
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        //Reject if cart is null or has no items
        if (lineItems == null || lineItems.isEmpty()) {
            return Optional.empty();
        }
        //Unwrap the Optional to get the actual User object
        User user = userOpt.get();
        //Prepare an empty list to hold valid order line items
        List<OrderLineItem> orderLines = new ArrayList<>();
        double total = 0.0;

        //Loop through each item in the cart
        for (Map<String, Object> raw : lineItems) {
            String snackId = String.valueOf(raw.get("snackId"));
            int quantity = parseQuantity(raw.get("quantity"));

            if (quantity <= 0) {
                continue;
            }

            Snack snack = snackService.getSnackById(snackId);
            if (snack == null) {
                return Optional.empty();
            }

            OrderLineItem line = new OrderLineItem(
                    snack.getSnackId(),
                    snack.getName(),
                    quantity,
                    snack.getPrice()
            );
            orderLines.add(line);
            total += line.getLineTotal();
        }

        if (orderLines.isEmpty()) {
            return Optional.empty();
        }

        SnackOrder order = new SnackOrder();
        order.setUserId(user.getId());
        String displayName = user.getUsername();
        order.setUsername(displayName != null && !displayName.isBlank() ? displayName : "Customer");
        order.setTotalAmount(total);
        order.setPlacedAt(Instant.now());
        order.setItems(orderLines);

        return Optional.of(snackOrderRepository.save(order));
    }

    private int parseQuantity(Object value) {
        // If value is already a Number type (Integer, Double, etc.) — convert directly
        if (value instanceof Number number) {
            return number.intValue();
        }
        // Otherwise try to parse it as a String (e.g. "2" → 2)
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException e) {
            // If parsing fails (e.g. "abc"), return 0 — treated as invalid
            return 0;
        }
    }
}
