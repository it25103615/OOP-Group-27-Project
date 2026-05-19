package com.example.cinema.repositories;

import com.example.cinema.models.Seat;
import java.util.List;

    // Interface defining all seat data operations
    public interface SeatRepository {

        // CREATE
        void save(Seat seat);

        // READ
        List<Seat> findAll();
        Seat findById(String seatId);
        List<Seat> findByTheaterId(String theaterId);

        // UPDATE
        boolean update(Seat seat);

        // DELETE
        boolean delete(String seatId);
        void deleteByTheaterId(String theaterId);
    }