package com.example.cinema.SnackApp;

import com.example.cinema.controllers.SnackController;
import com.example.cinema.models.Snack;
import com.example.cinema.services.SnackService;
import java.util.ArrayList;
import java.util.List;

// SnackApp is the main class to test the snack module
public class SnackApp {

    public static void main(String[] args) {

        // Create instances of controller and service
        SnackController controller   = new SnackController();
        SnackService    snackService = new SnackService();

        // Display all snacks
        System.out.println("\n===== DISPLAY ALL SNACKS =====");
        controller.displayAllSnacks();

        // Display a single snack by ID
        System.out.println("\n===== GET SNACK BY ID =====");
        controller.displaySnackById("S001");

        // Display snacks by category
        System.out.println("\n===== GET SNACKS BY CATEGORY =====");
        controller.displaySnacksByCategory("Beverage");

        // Add a new snack
        System.out.println("\n===== ADD NEW SNACK =====");
        snackService.addSnack("S009", "French Fries", 399.00,
                "Food", "images/fries.png");

        // Update an existing snack
        System.out.println("\n===== UPDATE SNACK =====");
        snackService.updateSnack("S001", "Large Popcorn", 599.00,
                "Food", "images/popcorn_large.png");

        // Delete a snack
        System.out.println("\n===== DELETE SNACK =====");
        snackService.deleteSnack("S008");

        // Calculate total cost of selected snacks
        System.out.println("\n===== CALCULATE TOTAL COST =====");
        List<Snack> selectedSnacks = new ArrayList<>();

        // Add snacks to the selected list
        selectedSnacks.add(snackService.getSnackById("S001"));
        selectedSnacks.add(snackService.getSnackById("S004"));
        selectedSnacks.add(snackService.getSnackById("S007"));

        // Display total cost of selected snacks
        controller.displayTotalCost(selectedSnacks);
    }
}