package com.example.cinema.repositories;

import com.example.cinema.models.Theater;
import java.util.List;

    // This interface defines WHAT operations are available for Theater data
// The actual reading/writing is done by FileHandler (our file-based storage)
// This keeps our code consistent with the rest of the team's repository pattern
    public interface TheaterRepository {

        // CREATE
        void save(Theater theater);

        // READ
        List<Theater> findAll();
        Theater findById(String id);
        List<Theater> findByName(String keyword);

        // UPDATE
        boolean update(Theater theater);

        // DELETE
        boolean delete(String id);
    }
