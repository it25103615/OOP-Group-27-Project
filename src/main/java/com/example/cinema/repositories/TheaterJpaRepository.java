package com.example.cinema.repositories;

import com.example.cinema.models.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Spring Data JPA access for Theater (H2). Does not replace TheaterRepository interface. */
@Repository
public interface TheaterJpaRepository extends JpaRepository<Theater, String> {
    List<Theater> findByNameContainingIgnoreCase(String keyword);
}
