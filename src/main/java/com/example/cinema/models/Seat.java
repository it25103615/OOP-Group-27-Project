package com.example.cinema.models;

    public class Seat {
        // Private attributes (Encapsulation)
        private int row;
        private int col;
        private String seatType;
        private double price;

        // Constructor
        public Seat(int row, int col, String seatType, double price) {
            this.row = row;
            this.col = col;
            this.seatType = seatType;
            this.price = price;
        }

        // Getters and Setters
        public int getRow() {
            return row;
        }
        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }
        public void setCol(int col) {
            this.col = col;
        }

        public String getSeatType() {
            return seatType;
        }
        public void setSeatType(String seatType) {
            this.seatType = seatType;
        }

        public double getPrice() {
            return price;
        }
        public void setPrice(double price) {
            this.price = price;
        }
    }

