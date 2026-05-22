package com.example.cinema.repositories;

import com.example.cinema.models.Theater;
import org.springframework.stereotype.Repository;

import java.util.List;

/** H2 implementation of TheaterRepository (team interface unchanged). */
@Repository
public class TheaterRepositoryImpl implements TheaterRepository {

    private final TheaterJpaRepository jpa;

    public TheaterRepositoryImpl(TheaterJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void save(Theater theater) {
        jpa.save(theater);
    }

    @Override
    public List<Theater> findAll() {
        return jpa.findAll();
    }

    @Override
    public Theater findById(String id) {
        return jpa.findById(id).orElse(null);
    }

    @Override
    public List<Theater> findByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        return jpa.findByNameContainingIgnoreCase(keyword.trim());
    }

    @Override
    public boolean update(Theater theater) {
        if (theater == null || theater.getId() == null || !jpa.existsById(theater.getId())) {
            return false;
        }
        jpa.save(theater);
        return true;
    }

    @Override
    public boolean delete(String id) {
        if (!jpa.existsById(id)) {
            return false;
        }
        jpa.deleteById(id);
        return true;
    }
}
