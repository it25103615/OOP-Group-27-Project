package com.example.cinema.services;

import com.example.cinema.models.Theater;
import com.example.cinema.util.FileHandler;

import java.util.List;
import java.util.stream.Collectors;

    public class TheaterService {

        // CREATE
        public String addTheater(String name, String location, int capacity,
                                 int screenCount, boolean hasAC, String theaterType) {
            if (name == null || name.trim().isEmpty()) return null;
            if (capacity <= 0 || screenCount <= 0)    return null;

            String id = FileHandler.generateTheaterId();
            Theater t = new Theater(id, name.trim(), location.trim(),
                    capacity, screenCount, hasAC, theaterType);
            FileHandler.appendTheater(t);
            return id;
        }

        // READ ALL
        public List<Theater> getAllTheaters() {
            return FileHandler.readAllTheaters();
        }

        // READ BY ID
        public Theater getTheaterById(String id) {
            return FileHandler.findTheaterById(id);
        }

        // SEARCH by name
        public List<Theater> searchByName(String keyword) {
            String kw = keyword.toLowerCase().trim();
            return FileHandler.readAllTheaters().stream()
                    .filter(t -> t.getName().toLowerCase().contains(kw))
                    .collect(Collectors.toList());
        }

        // UPDATE
        public boolean updateTheater(String id, String name, String location,
                                     int capacity, int screenCount,
                                     boolean hasAC, String theaterType) {
            Theater t = FileHandler.findTheaterById(id);
            if (t == null) return false;

            t.setName(name);
            t.setLocation(location);
            t.setCapacity(capacity);
            t.setScreenCount(screenCount);
            t.setHasAC(hasAC);
            t.setTheaterType(theaterType);
            return FileHandler.updateTheater(t);
        }

        // DELETE
        public boolean deleteTheater(String id) {
            return FileHandler.deleteTheater(id); // also deletes all its seats
        }

        // STATS
        public long getAvailableSeats(String theaterId) {
            return FileHandler.countAvailable(theaterId);
        }

        public long getReservedSeats(String theaterId) {
            return FileHandler.countReserved(theaterId);
        }
    }
