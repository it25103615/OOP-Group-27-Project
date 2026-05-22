package com.example.cinema.services;

import com.example.cinema.models.Booking;
import com.example.cinema.repositories.BookingJpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {

    public static final String STATUS_CONFIRMED = "CONFIRMED";

    private final BookingJpaRepository bookingRepository;
    private final SeatService seatService;

    public BookingService(BookingJpaRepository bookingRepository, SeatService seatService) {
        this.bookingRepository = bookingRepository;
        this.seatService = seatService;
    }

    /**
     * After payment: reserve seats and persist booking to H2.
     */
    public Booking confirmBooking(Map<String, Object> payload) {
        @SuppressWarnings("unchecked")
        List<String> seatIds = (List<String>) payload.get("seatIds");
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("No seats selected");
        }

        java.util.List<String> reserved = new java.util.ArrayList<>();
        for (String seatId : seatIds) {
            if (!seatService.reserveSeat(seatId)) {
                for (String r : reserved) {
                    seatService.releaseSeat(r);
                }
                throw new IllegalStateException("Seat not available: " + seatId);
            }
            reserved.add(seatId);
        }

        Booking b = new Booking();
        b.setBookingId(nextBookingId());
        b.setMovieTitle((String) payload.get("movieTitle"));
        b.setTheaterId((String) payload.get("theaterId"));
        b.setTheaterName((String) payload.get("theaterName"));
        b.setShowDate((String) payload.get("showDate"));
        b.setShowTime((String) payload.get("showTime"));
        b.setSeatLabels((String) payload.get("seatLabels"));
        b.setSeatIds(String.join(",", seatIds));
        b.setTicketCount(seatIds.size());
        Object total = payload.get("totalAmount");
        b.setTotalAmount(total instanceof Number ? ((Number) total).doubleValue() : 0);
        b.setUsername(payload.get("username") != null ? payload.get("username").toString() : "guest");
        b.setStatus(STATUS_CONFIRMED);
        b.setCreatedAt(LocalDateTime.now());
        return bookingRepository.save(b);
    }

    public Booking getById(String id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public List<Booking> findByUsername(String username) {
        if (username == null || username.isBlank()) {
            return List.of();
        }
        return bookingRepository.findByUsernameOrderByCreatedAtDesc(username);
    }

    private String nextBookingId() {
        int max = 0;
        for (Booking b : bookingRepository.findAll()) {
            try {
                int n = Integer.parseInt(b.getBookingId().substring(1));
                if (n > max) max = n;
            } catch (Exception ignored) {
            }
        }
        return String.format("B%05d", max + 1);
    }
}
