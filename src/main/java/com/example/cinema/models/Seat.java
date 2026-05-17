package com.example.cinema.models;

// OOP: Encapsulation — private fields with controlled getters/setters
//      Inheritance  — VIPSeat extends this class
public class Seat {

    // Constants prevent typos like "Vip" vs "VIP"
    public static final String TYPE_REGULAR    = "REGULAR";
    public static final String TYPE_VIP        = "VIP";
    public static final String TYPE_WHEELCHAIR = "WHEELCHAIR";

    public static final String STATUS_AVAILABLE = "AVAILABLE";
    public static final String STATUS_RESERVED  = "RESERVED";
    public static final String STATUS_BLOCKED   = "BLOCKED";

    private String seatId;
    private String theaterId;   // which theater this seat belongs to
    private String rowLabel;    // A, B, C ...
    private int    seatNumber;  // 1, 2, 3 ...
    private String seatType;
    private String status;
    private double price;

    public Seat() {
        this.status = STATUS_AVAILABLE; // new seats start as available
    }

    public Seat(String seatId, String theaterId, String rowLabel,
                int seatNumber, String seatType, double price) {
        this.seatId     = seatId;
        this.theaterId  = theaterId;
        this.rowLabel   = rowLabel;
        this.seatNumber = seatNumber;
        this.seatType   = seatType;
        this.price      = price;
        this.status     = STATUS_AVAILABLE;
    }

    // Reserve this seat — returns false if already reserved/blocked
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

    public boolean isAvailable() {
        return STATUS_AVAILABLE.equals(status);
    }

    // Convenience label e.g. "A-05"
    public String getSeatLabel() {
        return rowLabel + "-" + String.format("%02d", seatNumber);
    }

    // Get price — overridden in VIPSeat to add surcharge (Polymorphism)
    public double getPrice() { return price; }

    // Convert object → one line in seats.txt
    // Format:  seatId|theaterId|rowLabel|seatNumber|seatType|status|price
    public String toCsvLine() {
        return seatId + "|" + theaterId + "|" + rowLabel + "|"
                + seatNumber + "|" + seatType + "|" + status + "|" + price;
    }

    // Convert one line from seats.txt → Seat object
    public static Seat fromCsvLine(String line) {
        String[] p = line.split("\\|");
        if (p.length < 7) return null;
        Seat s = new Seat();
        s.setSeatId(p[0].trim());
        s.setTheaterId(p[1].trim());
        s.setRowLabel(p[2].trim());
        s.setSeatNumber(Integer.parseInt(p[3].trim()));
        s.setSeatType(p[4].trim());
        s.setStatus(p[5].trim());
        s.price = Double.parseDouble(p[6].trim());
        return s;
    }

    // Getters
    public String getSeatId()    { return seatId; }
    public String getTheaterId() { return theaterId; }
    public String getRowLabel()  { return rowLabel; }
    public int    getSeatNumber(){ return seatNumber; }
    public String getSeatType()  { return seatType; }
    public String getStatus()    { return status; }

    // Setters
    public void setSeatId(String seatId)       { this.seatId = seatId; }
    public void setTheaterId(String theaterId) { this.theaterId = theaterId; }
    public void setRowLabel(String rowLabel)   { this.rowLabel = rowLabel; }
    public void setSeatNumber(int seatNumber)  { this.seatNumber = seatNumber; }
    public void setSeatType(String seatType)   { this.seatType = seatType; }
    public void setStatus(String status)       { this.status = status; }
    public void setPrice(double price)         { this.price = price; }
}
