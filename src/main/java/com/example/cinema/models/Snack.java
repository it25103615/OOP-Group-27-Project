package com.example.cinema.models;

public class Snack {

    private String snackId;
    private String name;
    private double price;
    private String category;
    private String imagePath;

    public Snack() {
        this.snackId   = "";
        this.name      = "";
        this.price     = 0.0;
        this.category  = "";
        this.imagePath = "";
    }

    public Snack(String snackId, String name, double price,
                 String category, String imagePath) {
        this.snackId   = snackId;
        this.name      = name;
        this.price     = price;
        this.category  = category;
        this.imagePath = imagePath;
    }

    public String getSnackId() {
        return snackId;
    }

    public void setSnackId(String snackId) {
        this.snackId = snackId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

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