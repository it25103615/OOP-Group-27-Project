package com.example.cinema.repositories;

import com.example.cinema.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingJpaRepository extends JpaRepository<Booking, String> {
    List<Booking> findByUsernameOrderByCreatedAtDesc(String username);
}
