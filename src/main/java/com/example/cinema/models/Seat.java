package com.example.cinema.models;

public class Seat implements Seatble {

    private String seatId, theaterId, rowLabel, seatType, status;
    private int seatNumber;
    private double price;

    public Seat() {
        status = "AVAILABLE";
    }

    public Seat(String seatId, String theaterId, String rowLabel,
                int seatNumber, String seatType, double price) {

        this.seatId = seatId;
        this.theaterId = theaterId;
        this.rowLabel = rowLabel;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.price = price;
        this.status = "AVAILABLE";
    }

    @Override
    public boolean reserve() {

        if (status.equals("AVAILABLE")) {
            status = "RESERVED";
            return true;
        }

        return false;
    }

    @Override
    public boolean release() {

        if (status.equals("RESERVED")) {
            status = "AVAILABLE";
            return true;
        }

        return false;
    }

    @Override
    public boolean isAvailable() {
        return status.equals("AVAILABLE");
    }

    public String toCsvLine() {

        return seatId + "|" + theaterId + "|" + rowLabel + "|"
                + seatNumber + "|" + seatType + "|"
                + status + "|" + price;
    }

    public static Seat fromCsvLine(String line) {

        String[] p = line.split("\\|");

        Seat s = new Seat();

        s.setSeatId(p[0]);
        s.setTheaterId(p[1]);
        s.setRowLabel(p[2]);
        s.setSeatNumber(Integer.parseInt(p[3]));
        s.setSeatType(p[4]);
        s.setStatus(p[5]);
        s.setPrice(Double.parseDouble(p[6]));

        return s;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public String getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(String theaterId) {
        this.theaterId = theaterId;
    }

    public String getRowLabel() {
        return rowLabel;
    }

    public void setRowLabel(String rowLabel) {
        this.rowLabel = rowLabel;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
