package com.example.cinema.repositories;

import com.example.cinema.models.SnackOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnackOrderRepository extends JpaRepository<SnackOrder, Long> {
}
