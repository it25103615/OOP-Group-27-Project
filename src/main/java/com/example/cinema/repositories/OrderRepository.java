package com.example.cinema.repositories;

import com.example.cinema.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Custom query to find all orders by a specific customer
    List<Order> findByCustomerId(Long customerId);
}
