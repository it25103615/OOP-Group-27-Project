package com.example.cinema.repositories;

import com.example.cinema.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Spring Data JPA access for Seat (H2). Does not replace SeatRepository interface. */
@Repository
public interface SeatJpaRepository extends JpaRepository<Seat, String> {
    List<Seat> findByTheaterId(String theaterId);
    void deleteByTheaterId(String theaterId);
    long countByTheaterIdAndStatus(String theaterId, String status);
}
