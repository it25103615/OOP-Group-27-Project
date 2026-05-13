package com.example.cinema.models;

    public abstract class Venue {

        private String id;
        private String name;
        private String location;
        private int capacity;

        public Venue() {
        }

        public Venue(String id, String name, String location, int capacity) {
            this.id = id;
            this.name = name;
            this.location = location;
            this.capacity = capacity;
        }

        public abstract String getVenueType();

        public abstract String getDisplayInfo();

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

        public String toCsvLine() {
            return id + "|" + name + "|" + location + "|" + capacity;
        }

        @Override
        public String toString() {
            return "Venue{id='" + id + "', name='" + name
                    + "', type=" + getVenueType() + "'}";
        }
    }
