package com.example.cinema.models;

// OOP: Encapsulation — all fields are private, accessed only through getters/setters
public class Theater {

    private String  id;
    private String  name;
    private String  location;
    private int     capacity;
    private int     screenCount;
    private boolean hasAC;
    private String  theaterType;  // MULTIPLEX | SINGLE_SCREEN | DRIVE_IN

    // Default constructor (needed when reading from file)
    public Theater() {}

    // Full constructor
    public Theater(String id, String name, String location, int capacity,
                   int screenCount, boolean hasAC, String theaterType) {
        this.id          = id;
        this.name        = name;
        this.location    = location;
        this.capacity    = capacity;
        this.screenCount = screenCount;
        this.hasAC       = hasAC;
        this.theaterType = theaterType;
    }

    // Convert object → one line in theaters.txt
    // Format:  id|name|location|capacity|screenCount|hasAC|theaterType
    public String toCsvLine() {
        return id + "|" + name + "|" + location + "|" + capacity
                + "|" + screenCount + "|" + hasAC + "|" + theaterType;
    }

    // Convert one line from theaters.txt → Theater object
    public static Theater fromCsvLine(String line) {
        String[] p = line.split("\\|");
        if (p.length < 7) return null;
        return new Theater(
                p[0].trim(), p[1].trim(), p[2].trim(),
                Integer.parseInt(p[3].trim()),
                Integer.parseInt(p[4].trim()),
                Boolean.parseBoolean(p[5].trim()),
                p[6].trim()
        );
    }

    // Getters
    public String  getId(){

        return id;
    }
    public String  getName()        {
        return name; }
    public String  getLocation()    {
        return location; }
    public int     getCapacity()    { return capacity; }
    public int     getScreenCount() { return screenCount; }
    public boolean isHasAC()        { return hasAC; }
    public String  getTheaterType() { return theaterType; }

    // Setters
    public void setId(String id)                   { this.id = id; }
    public void setName(String name)               { this.name = name; }
    public void setLocation(String location)       { this.location = location; }
    public void setCapacity(int capacity)          { this.capacity = capacity; }
    public void setScreenCount(int screenCount)    { this.screenCount = screenCount; }
    public void setHasAC(boolean hasAC)            { this.hasAC = hasAC; }
    public void setTheaterType(String theaterType) { this.theaterType = theaterType; }
}