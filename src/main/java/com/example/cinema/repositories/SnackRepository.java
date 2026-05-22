package com.example.cinema.repositories;

import com.example.cinema.models.Snack;
import org.springframework.data.jpa.repository.JpaRepository; //a powerful Spring interface that gives you database methods for free
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
// Tells Spring this is a data access layer component
//Spring manages it and allows it to be @Autowired into services
//Also automatically converts database errors into Spring exceptions

public interface SnackRepository extends JpaRepository<Snack, String> {
    List<Snack> findByCategory(String category);

}