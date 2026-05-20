package com.example.cinema.repositories;

import com.example.cinema.models.Seat;
import org.springframework.stereotype.Repository;

import java.util.List;

/** H2 implementation of SeatRepository (team interface unchanged). */
@Repository
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatJpaRepository jpa;

    public SeatRepositoryImpl(SeatJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void save(Seat seat) {
        jpa.save(seat);
    }

    @Override
    public List<Seat> findAll() {
        return jpa.findAll();
    }

    @Override
    public Seat findById(String seatId) {
        return jpa.findById(seatId).orElse(null);
    }

    @Override
    public List<Seat> findByTheaterId(String theaterId) {
        return jpa.findByTheaterId(theaterId);
    }

    @Override
    public boolean update(Seat seat) {
        if (seat == null || seat.getSeatId() == null || !jpa.existsById(seat.getSeatId())) {
            return false;
        }
        jpa.save(seat);
        return true;
    }

    @Override
    public boolean delete(String seatId) {
        if (!jpa.existsById(seatId)) {
            return false;
        }
        jpa.deleteById(seatId);
        return true;
    }

    @Override
    public void deleteByTheaterId(String theaterId) {
        jpa.deleteByTheaterId(theaterId);
    }
}
