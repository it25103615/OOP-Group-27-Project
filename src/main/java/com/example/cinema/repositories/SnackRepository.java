package com.example.cinema.repositories;

import java.util.ArrayList;
import java.util.List;
import com.example.cinema.models.Snack;


public class SnackRepository {

    private List<Snack> snackList = new ArrayList<>();

    public SnackRepository() {
        snackList.add(new Snack("S001", "Popcorn",       350.00, "Food",     "images/popcorn.png"));
        snackList.add(new Snack("S002", "Nachos",        450.00, "Food",     "images/nachos.png"));
        snackList.add(new Snack("S003", "Hot Dog",       550.00, "Food",     "images/hotdog.png"));
        snackList.add(new Snack("S004", "Coca Cola",     250.00, "Beverage", "images/cola.png"));
        snackList.add(new Snack("S005", "Orange Juice",  300.00, "Beverage", "images/juice.png"));
        snackList.add(new Snack("S006", "Water Bottle",  150.00, "Beverage", "images/water.png"));
        snackList.add(new Snack("S007", "Candy",         100.00, "Sweets",   "images/candy.png"));
        snackList.add(new Snack("S008", "Chocolate Bar", 200.00, "Sweets",   "images/chocolate.png"));
    }

    public List<Snack> getAllSnacks() {
        return snackList;
    }

    public Snack getSnackById(String snackId) {
        for (Snack snack : snackList) {
            if (snack.getSnackId().equals(snackId)) {
                return snack;
            }
        }
        return null;
    }

    public List<Snack> getSnacksByCategory(String category) {
        List<Snack> result = new ArrayList<>();
        for (Snack snack : snackList) {
            if (snack.getCategory().equalsIgnoreCase(category)) {
                result.add(snack);
            }
        }
        return result;
    }

    public boolean addSnack(Snack snack) {
        if (getSnackById(snack.getSnackId()) != null) {
            System.out.println("Snack with ID " + snack.getSnackId() + " already exists.");
            return false;
        }
        snackList.add(snack);
        return true;
    }

    public boolean updateSnack(String snackId, Snack updatedSnack) {
        for (int i = 0; i < snackList.size(); i++) {
            if (snackList.get(i).getSnackId().equals(snackId)) {
                snackList.set(i, updatedSnack);
                return true;
            }
        }
        return false;
    }

    public boolean deleteSnack(String snackId) {
        for (int i = 0; i < snackList.size(); i++) {
            if (snackList.get(i).getSnackId().equals(snackId)) {
                snackList.remove(i);
                return true;
            }
        }
        return false;
    }
}