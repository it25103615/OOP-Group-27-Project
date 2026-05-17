package com.example.cinema.repositories;

import com.example.cinema.models.Snack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SnackRepository extends JpaRepository<Snack, String> {
    List<Snack> findByCategory(String category);

}