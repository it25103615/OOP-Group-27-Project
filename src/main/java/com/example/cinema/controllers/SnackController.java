package com.example.cinema.controllers;

import com.example.cinema.models.Snack;
import com.example.cinema.services.SnackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/snacks")
public class SnackController {

    @Autowired
    private SnackService snackService;

    // GET all snacks
    // URL: GET http://localhost:8080/api/snacks
    @GetMapping
    public ResponseEntity<List<Snack>> getAllSnacks() {
        return ResponseEntity.ok(snackService.getAllSnacks());
    }

    // GET snack by ID
    // URL: GET http://localhost:8080/api/snacks/S001
    @GetMapping("/{snackId}")
    public ResponseEntity<Snack> getSnackById(@PathVariable String snackId) {
        Snack snack = snackService.getSnackById(snackId);
        if (snack == null) {
            return ResponseEntity.notFound().build(); // 404
        }
        return ResponseEntity.ok(snack); // 200
    }

    // GET snacks by category
    // URL: GET http://localhost:8080/api/snacks/category/Food
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Snack>> getSnacksByCategory(@PathVariable String category) {
        return ResponseEntity.ok(snackService.getSnacksByCategory(category));
    }

    // POST add new snack
    // URL: POST http://localhost:8080/api/snacks
    // Body: { "snackId": "S009", "name": "French Fries", "price": 399.00, "category": "Food", "imagePath": "images/fries.png" }
    @PostMapping
    public ResponseEntity<String> addSnack(@RequestBody Snack snack) {
        boolean success = snackService.addSnack(
                snack.getSnackId(),
                snack.getName(),
                snack.getPrice(),
                snack.getCategory(),
                snack.getImagePath()
        );
        if (!success) {
            return ResponseEntity.badRequest().body("Snack with ID " + snack.getSnackId() + " already exists."); // 400
        }
        return ResponseEntity.ok("Snack added successfully: " + snack.getName()); // 200
    }

    // PUT update existing snack
    // URL: PUT http://localhost:8080/api/snacks/S001
    // Body: { "snackId": "S001", "name": "Large Popcorn", "price": 599.00, "category": "Food", "imagePath": "images/popcorn_large.png" }
    @PutMapping("/{snackId}")
    public ResponseEntity<String> updateSnack(@PathVariable String snackId,
                                              @RequestBody Snack snack) {
        boolean success = snackService.updateSnack(
                snackId,
                snack.getName(),
                snack.getPrice(),
                snack.getCategory(),
                snack.getImagePath()
        );
        if (!success) {
            return ResponseEntity.notFound().build(); // 404
        }
        return ResponseEntity.ok("Snack updated successfully: " + snack.getName()); // 200
    }

    // DELETE snack by ID
    // URL: DELETE http://localhost:8080/api/snacks/S001
    @DeleteMapping("/{snackId}")
    public ResponseEntity<String> deleteSnack(@PathVariable String snackId) {
        boolean success = snackService.deleteSnack(snackId);
        if (!success) {
            return ResponseEntity.notFound().build(); // 404
        }
        return ResponseEntity.ok("Snack deleted successfully with ID: " + snackId); // 200
    }


}