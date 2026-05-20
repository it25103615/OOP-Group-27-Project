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
     * @param userId authenticated user id from session
     * @param lineItems each map: snackId (String), quantity (Number)
     * @return saved order, or empty if validation fails
     */
    public Optional<SnackOrder> placeOrder(Long userId, List<Map<String, Object>> lineItems) {
        if (userId == null) {
            return Optional.empty();
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        if (lineItems == null || lineItems.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();
        List<OrderLineItem> orderLines = new ArrayList<>();
        double total = 0.0;

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
        order.setUsername(user.getUsername());
        order.setTotalAmount(total);
        order.setPlacedAt(Instant.now());
        order.setItems(orderLines);

        return Optional.of(snackOrderRepository.save(order));
    }

    private int parseQuantity(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
