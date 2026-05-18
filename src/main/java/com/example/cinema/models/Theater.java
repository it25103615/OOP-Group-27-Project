package com.example.cinema.models;

// OOP: Encapsulation
public class Theater {

    private String id;
    private String name;
    private String location;
    private int capacity;

    // Default constructor
    public Theater() {
    }

    // Full constructor
    public Theater(String id, String name,
                   String location, int capacity) {

        this.id = id;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
    }

    // Convert object to text line
    public String toCsvLine() {
        return id + "|" + name + "|" + location + "|" + capacity;
    }

    // Convert text line to object
    public static Theater fromCsvLine(String line) {
        String[] p = line.split("\\|");

        if (p.length < 4) {
            return null;
        }

        return new Theater(
                p[0].trim(),
                p[1].trim(),
                p[2].trim(),
                Integer.parseInt(p[3].trim())
        );
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}