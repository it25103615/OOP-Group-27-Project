package com.example.cinema.services;

import com.example.cinema.models.Snack;
import com.example.cinema.repositories.SnackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SnackService {

    @Autowired
    private SnackRepository snackRepository;

    // Returns all snacks from the database
    public List<Snack> getAllSnacks() {
        return snackRepository.findAll();
    }

    // Returns a snack by ID, or null if not found
    public Snack getSnackById(String snackId) {
        if (snackId == null || snackId.isEmpty()) {
            System.out.println("Invalid Snack ID.");
            return null;
        }
        Optional<Snack> snack = snackRepository.findById(snackId);
        if (snack.isEmpty()) {
            System.out.println("Snack not found with ID: " + snackId);
        }
        return snack;
    }

    public List<Snack> getSnacksByCategory(String category) {
        if (category == null || category.isEmpty()) {
            System.out.println("Invalid category.");
            return new ArrayList<>();
        }
        return snackRepository.getSnacksByCategory(category);
    }

    public boolean addSnack(String snackId, String name, double price,
                            String category, String imagePath) {
        if (snackId == null || snackId.isEmpty()) {
            System.out.println("Snack ID cannot be empty.");
            return false;
        }
        if (name == null || name.isEmpty()) {
            System.out.println("Snack name cannot be empty.");
            return false;
        }
        if (price <= 0) {
            System.out.println("Price must be greater than zero.");
            return false;
        }
        if (category == null || category.isEmpty()) {
            System.out.println("Category cannot be empty.");
            return false;
        }
        Snack newSnack = new Snack(snackId, name, price, category, imagePath);
        boolean success = snackRepository.addSnack(newSnack);
        if (success) {
            System.out.println("Snack added successfully: " + name);
        }
        return success;
    }

    public boolean updateSnack(String snackId, String name, double price,
                               String category, String imagePath) {
        if (snackRepository.getSnackById(snackId) == null) {
            System.out.println("Snack not found with ID: " + snackId);
            return false;
        }
        Snack updatedSnack = new Snack(snackId, name, price, category, imagePath);
        boolean success = snackRepository.updateSnack(snackId, updatedSnack);
        if (success) {
            System.out.println("Snack updated successfully: " + name);
        }
        return success;
    }

    public boolean deleteSnack(String snackId) {
        if (snackRepository.getSnackById(snackId) == null) {
            System.out.println("Snack not found with ID: " + snackId);
            return false;
        }
        boolean success = snackRepository.deleteSnack(snackId);
        if (success) {
            System.out.println("Snack deleted successfully with ID: " + snackId);
        }
        return success;
    }

    public double calculateTotalSnackCost(List<Snack> selectedSnacks) {
        double total = 0.0;
        for (Snack snack : selectedSnacks) {
            total += snack.getPrice();
        }
        return total;
    }
}