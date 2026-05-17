package com.example.cinema.controllers;

import com.example.cinema.models.Snack;
import com.example.cinema.services.SnackService;
import java.util.List;

public class SnackController {

    private SnackService snackService = new SnackService();

    public void displayAllSnacks() {
        List<Snack> snacks = snackService.getAllSnacks();
        System.out.println("==============================");
        System.out.println("        ALL SNACKS            ");
        System.out.println("==============================");
        for (Snack snack : snacks) {
            System.out.println("ID       : " + snack.getSnackId());
            System.out.println("Name     : " + snack.getName());
            System.out.println("Price    : Rs. " + snack.getPrice());
            System.out.println("Category : " + snack.getCategory());
            System.out.println("Image    : " + snack.getImagePath());
            System.out.println("------------------------------");
        }
    }

    public void displaySnackById(String snackId) {
        Snack snack = snackService.getSnackById(snackId);
        if (snack != null) {
            System.out.println("==============================");
            System.out.println("ID       : " + snack.getSnackId());
            System.out.println("Name     : " + snack.getName());
            System.out.println("Price    : Rs. " + snack.getPrice());
            System.out.println("Category : " + snack.getCategory());
            System.out.println("Image    : " + snack.getImagePath());
            System.out.println("==============================");
        }
    }

    public void displaySnacksByCategory(String category) {
        List<Snack> snacks = snackService.getSnacksByCategory(category);
        System.out.println("==============================");
        System.out.println("  SNACKS IN CATEGORY: " + category);
        System.out.println("==============================");
        for (Snack snack : snacks) {
            System.out.println("ID       : " + snack.getSnackId());
            System.out.println("Name     : " + snack.getName());
            System.out.println("Price    : Rs. " + snack.getPrice());
            System.out.println("------------------------------");
        }
    }

    public void addSnack(String snackId, String name, double price,
                         String category, String imagePath) {
        snackService.addSnack(snackId, name, price, category, imagePath);
    }

    public void updateSnack(String snackId, String name, double price,
                            String category, String imagePath) {
        snackService.updateSnack(snackId, name, price, category, imagePath);
    }

    public void deleteSnack(String snackId) {
        snackService.deleteSnack(snackId);
    }

    public void displayTotalCost(List<Snack> selectedSnacks) {
        double total = snackService.calculateTotalSnackCost(selectedSnacks);
        System.out.println("Total Snack Cost : Rs. " + total);
    }
}