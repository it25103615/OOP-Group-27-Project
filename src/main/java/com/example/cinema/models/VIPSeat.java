package com.example.cinema.models;

    // OOP: Inheritance  — VIPSeat IS-A Seat (reuses all Seat fields and methods)
//      Polymorphism — overrides getPrice() and reserve() with VIP-specific behaviour
    public class VIPSeat extends Seat {

        private boolean hasRecliner;
        private String  loungeName;

        public VIPSeat() {
            super();
            setSeatType(TYPE_VIP); // automatically mark as VIP type
        }

        public VIPSeat(String seatId, String theaterId, String rowLabel,
                       int seatNumber, double price,
                       boolean hasRecliner, String loungeName) {
            super(seatId, theaterId, rowLabel, seatNumber, TYPE_VIP, price);
            this.hasRecliner = hasRecliner;
            this.loungeName  = loungeName;
        }

        // OOP Polymorphism: same method name, different behaviour
        // VIP seats cost 15% more than the base price
        @Override
        public double getPrice() {
            return super.getPrice() * 1.15;
        }

        // OOP Polymorphism: VIP reserve() adds lounge notification on top of base logic
        @Override
        public boolean reserve() {
            boolean result = super.reserve(); // run parent logic first
            if (result) {
                System.out.println("VIP seat " + getSeatLabel()
                        + " reserved — Lounge: " + loungeName);
            }
            return result;
        }

        // Getters & Setters
        public boolean isHasRecliner()              { return hasRecliner; }
        public String  getLoungeName()              { return loungeName; }
        public void    setHasRecliner(boolean v)    { this.hasRecliner = v; }
        public void    setLoungeName(String v)      { this.loungeName = v; }
    }
