package com.example.cinema.models;

// Snack model class represents a snack item in the cinema
public class Snack {

    // Private attributes for snack details
    private String snackId;
    private String name;
    private double price;
    private String category;
    private String imagePath;

    // Default constructor - initializes snack with empty value
    public Snack() {
        this.snackId   = "";
        this.name      = "";
        this.price     = 0.0;
        this.category  = "";
        this.imagePath = "";
    }

    // Parameterized constructor  initializes snack with given values
    public Snack(String snackId, String name, double price,
                 String category, String imagePath) {
        this.snackId   = snackId;
        this.name      = name;
        this.price     = price;
        this.category  = category;
        this.imagePath = imagePath;
    }

    // Getter for snack ID
    public String getSnackId() {
        return snackId;
    }

    // Setter for snack ID
    public void setSnackId(String snackId) {
        this.snackId = snackId;
    }

    // Getter for snack name
    public String getName() {
        return name;
    }

    // Setter for snack name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for snack price
    public double getPrice() {
        return price;
    }

    // Setter for snack price
    public void setPrice(double price) {
        this.price = price;
    }

    // Getter for snack category
    public String getCategory() {
        return category;
    }

    // Setter for snack category
    public void setCategory(String category) {
        this.category = category;
    }

    // Getter for snack image path
    public String getImagePath() {
        return imagePath;
    }

    // Setter for snack image path
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // Returns a string representation of the snack object
    public String toString() {
        return "Snack{" +
                "snackId='"    + snackId   + '\'' +
                ", name='"     + name      + '\'' +
                ", price="     + price     +
                ", category='" + category  + '\'' +
                ", imagePath='"+ imagePath + '\'' +
                '}';
    }
}