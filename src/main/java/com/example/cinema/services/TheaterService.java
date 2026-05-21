package com.example.cinema.services;

import com.example.cinema.models.Theater;
import com.example.cinema.repositories.SeatRepository;
import com.example.cinema.repositories.TheaterJpaRepository;
import com.example.cinema.repositories.TheaterRepository;
import com.example.cinema.util.TheaterImages;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final TheaterJpaRepository theaterJpaRepository;
    private final SeatRepository seatRepository;

    public TheaterService(TheaterRepository theaterRepository,
                          TheaterJpaRepository theaterJpaRepository,
                          SeatRepository seatRepository) {
        this.theaterRepository = theaterRepository;
        this.theaterJpaRepository = theaterJpaRepository;
        this.seatRepository = seatRepository;
    }

    public String addTheater(String name, String location, int capacity) {
        return addTheater(name, location, capacity, null);
    }

    public String addTheater(String name, String location, int capacity, String imagePath) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        String id = nextTheaterId();
        Theater t = new Theater(id, name.trim(), location.trim(), capacity);
        if (imagePath != null && !imagePath.isBlank()) {
            t.setImagePath(imagePath.trim());
        } else {
            t.setImagePath(TheaterImages.pathForIndex(theaterRepository.findAll().size()));
        }
        theaterRepository.save(t);
        return id;
    }

    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    public Theater getTheaterById(String id) {
        return theaterRepository.findById(id);
    }

    public List<Theater> searchByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllTheaters();
        }
        String kw = keyword.toLowerCase().trim();
        return theaterRepository.findAll().stream()
                .filter(t -> t.getName().toLowerCase().contains(kw)
                        || (t.getLocation() != null && t.getLocation().toLowerCase().contains(kw)))
                .collect(Collectors.toList());
    }

    public boolean updateTheater(String id, String name, String location, int capacity) {
        Theater t = theaterRepository.findById(id);
        if (t == null) {
            return false;
        }
        t.setName(name);
        t.setLocation(location);
        t.setCapacity(capacity);
        return theaterRepository.update(t);
    }

    public boolean deleteTheater(String id) {
        seatRepository.deleteByTheaterId(id);
        return theaterRepository.delete(id);
    }

    public long getAvailableSeats(String theaterId) {
        return seatRepository.findByTheaterId(theaterId).stream()
                .filter(s -> com.example.cinema.models.Seat.STATUS_AVAILABLE.equals(s.getStatus()))
                .count();
    }

    public long getReservedSeats(String theaterId) {
        return seatRepository.findByTheaterId(theaterId).stream()
                .filter(s -> com.example.cinema.models.Seat.STATUS_RESERVED.equals(s.getStatus()))
                .count();
    }

    public boolean isEmpty() {
        return theaterJpaRepository.count() == 0;
    }

    /** Assigns static/images paths to theaters missing imagePath (H2 backfill). */
    public void backfillTheaterImages() {
        List<Theater> theaters = theaterRepository.findAll();
        for (int i = 0; i < theaters.size(); i++) {
            Theater t = theaters.get(i);
            if (t.getImagePath() == null || t.getImagePath().isBlank()) {
                t.setImagePath(TheaterImages.pathForIndex(i));
                theaterRepository.update(t);
            }
        }
    }

    private String nextTheaterId() {
        int max = 0;
        for (Theater t : theaterRepository.findAll()) {
            try {
                int n = Integer.parseInt(t.getId().substring(1));
                if (n > max) {
                    max = n;
                }
            } catch (Exception ignored) {
            }
        }
        return String.format("T%03d", max + 1);
    }
}
