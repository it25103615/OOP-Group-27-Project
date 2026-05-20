package com.example.cinema.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Encapsulation — private fields with controlled getters/setters
// Inheritance  — VIPSeat extends this class
@Entity
@Table(name = "seats")
public class Seat {

    // Constants prevent types like "Vip" vs "VIP"
    public static final String TYPE_REGULAR    = "REGULAR";
    public static final String TYPE_VIP        = "VIP";

    public static final String STATUS_AVAILABLE = "AVAILABLE";
    public static final String STATUS_RESERVED  = "RESERVED";

    @Id
    private String seatId;
    private String theaterId;   // which theater this seat belongs to
    @Column(name = "row_label")
    private String row;    // A, B, C ...
    private int seatNumber;  // 1, 2, 3 ...
    private String seatType; //regular or VIP
    private String status;
    private double price;

    //if empty seat is created first and fill details later
    public Seat() {
        this.status = STATUS_AVAILABLE; // new seats start as available
    }

    //if new seat is created with full details
    public Seat(String seatId, String theaterId, String row,int seatNumber, String seatType, double price) {
        this.seatId = seatId;
        this.theaterId = theaterId;
        this.row = row;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.price = price;
        this.status = STATUS_AVAILABLE;
    }

    public boolean isAvailable() {
        return STATUS_AVAILABLE.equals(status);
    }

    // Reserve this seat — returns false if already reserved
    public boolean reserve() {
        if (STATUS_AVAILABLE.equals(status)) {
            status = STATUS_RESERVED;
            return true;
        }
        return false;
    }

    // Release reservation back to available
    public boolean release() {
        if (STATUS_RESERVED.equals(status)) {
            status = STATUS_AVAILABLE;
            return true;
        }
        return false;
    }

    // Convenience label e.g. "A-05"
    public String getSeatLabel() {
        return row + "-" + String.format("%02d", seatNumber);
    }

    // Get price — overridden in VIPSeat to add surcharge (Polymorphism)
    public double getPrice() {
        return price;
    }

    // Convert object → one line in seats.txt
    // Format:  seatId|theaterId|rowLabel|seatNumber|seatType|status|price
    public String toCsvLine() {
        return seatId + "|" + theaterId + "|" + row + "|"
                + seatNumber + "|" + seatType + "|" + status + "|" + price;
    }

    // Convert one line from seats.txt → Seat object
    public static Seat fromCsvLine(String line) {
        String[] p = line.split("\\|");
        if (p.length < 7) return null;
        Seat s = new Seat();
        s.setSeatId(p[0].trim());
        s.setTheaterId(p[1].trim());
        s.setRow(p[2].trim());
        s.setSeatNumber(Integer.parseInt(p[3].trim()));
        s.setSeatType(p[4].trim());
        s.setStatus(p[5].trim());
        s.price = Double.parseDouble(p[6].trim());
        return s;
    }

    // Getters
    public String getSeatId() {
        return seatId;
    }
    public String getTheaterId(){
        return theaterId;
    }
    public String getRow(){
        return row;
    }
    public int    getSeatNumber(){
        return seatNumber;
    }
    public String getSeatType(){
        return seatType;
    }
    public String getStatus(){
        return status;
    }

    // Setters
    public void setSeatId(String seatId){
        this.seatId = seatId;
    }
    public void setTheaterId(String theaterId){
        this.theaterId = theaterId;
    }
    public void setRow(String row){
        this.row = row;
    }
    public void setSeatNumber(int seatNumber){
        this.seatNumber = seatNumber;
    }
    public void setSeatType(String seatType){
        this.seatType = seatType;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public void setPrice(double price){
        this.price = price;
    }
}
