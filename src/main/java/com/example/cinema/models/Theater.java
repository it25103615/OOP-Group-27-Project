package com.example.cinema.models;

public class Theater extends Venue {

    private int screenCount;
    private boolean hasAC;
    private String theaterType;

    public Theater() {
    }

    public Theater(String id, String name, String location, int capacity,
                   int screenCount, boolean hasAC, String theaterType) {

        super(id, name, location, capacity);

        this.screenCount = screenCount;
        this.hasAC = hasAC;
        this.theaterType = theaterType;
    }

    @Override
    public String getVenueType() {
        return "Theater";
    }

    @Override
    public String getDisplayInfo() {

        return getName() + " | "
                + theaterType + " | "
                + screenCount + " Screens | "
                + (hasAC ? "AC" : "No AC");
    }

    @Override
    public String toCsvLine() {

        return super.toCsvLine() + "|"
                + screenCount + "|"
                + hasAC + "|"
                + theaterType;
    }

    public static Theater fromCsvLine(String line) {

        String[] p = line.split("\\|");

        Theater t = new Theater();

        t.setId(p[0]);
        t.setName(p[1]);
        t.setLocation(p[2]);
        t.setCapacity(Integer.parseInt(p[3]));
        t.setScreenCount(Integer.parseInt(p[4]));
        t.setHasAC(Boolean.parseBoolean(p[5]));
        t.setTheaterType(p[6]);

        return t;
    }

    public int getScreenCount() {
        return screenCount;
    }

    public void setScreenCount(int screenCount) {
        this.screenCount = screenCount;
    }

    public boolean isHasAC() {
        return hasAC;
    }

    public void setHasAC(boolean hasAC) {
        this.hasAC = hasAC;
    }

    public String getTheaterType() {
        return theaterType;
    }

    public void setTheaterType(String theaterType) {
        this.theaterType = theaterType;
    }
}