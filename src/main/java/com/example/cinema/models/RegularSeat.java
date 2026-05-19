package com.example.cinema.models;

    // OOP: Inheritance  — RegularSeat IS-A Seat
//      Polymorphism — overrides getPrice() just like VIPSeat does,
//                     so the same code works for both types
    public class RegularSeat extends Seat {

        private boolean isWindowSeat; // extra field only regular seats care about

        public RegularSeat() {
            super();
            setSeatType(TYPE_REGULAR); // automatically mark as REGULAR
        }

        public RegularSeat(String seatId, String theaterId, String rowLabel,
                           int seatNumber, double price, boolean isWindowSeat) {
            super(seatId, theaterId, rowLabel, seatNumber, TYPE_REGULAR, price);
            this.isWindowSeat = isWindowSeat;
        }

        // OOP Polymorphism: same method name as VIPSeat, but no surcharge here
        // Regular seats are sold at face value
        @Override
        public double getPrice() {
            return super.getPrice(); // no extra charge
        }

        // OOP Polymorphism: regular reserve just uses the base logic — no extra steps
        @Override
        public boolean reserve() {
            return super.reserve();
        }

        public boolean isWindowSeat()            { return isWindowSeat; }
        public void    setWindowSeat(boolean v)  { this.isWindowSeat = v; }
    }

