package com.example.cinema.services;

import com.example.cinema.models.Seat;
import com.example.cinema.repositories.SeatJpaRepository;
import com.example.cinema.repositories.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final SeatJpaRepository seatJpaRepository;

    public SeatService(SeatRepository seatRepository, SeatJpaRepository seatJpaRepository) {
        this.seatRepository = seatRepository;
        this.seatJpaRepository = seatJpaRepository;
    }

    public String addSeat(String theaterId, String rowLabel,
                          int seatNumber, String seatType, double price) {
        if (theaterId == null || theaterId.isEmpty()) return null;
        if (rowLabel == null || rowLabel.isEmpty()) return null;
        if (seatNumber <= 0 || price < 0) return null;

        for (Seat s : seatRepository.findByTheaterId(theaterId)) {
            if (s.getRow().equalsIgnoreCase(rowLabel) && s.getSeatNumber() == seatNumber) {
                return null;
            }
        }

        String id = nextSeatId();
        Seat seat = new Seat(id, theaterId, rowLabel.toUpperCase(), seatNumber, seatType, price);
        seatRepository.save(seat);
        return id;
    }

    public int bulkAddSeats(String theaterId, String fromRow, String toRow,
                            int seatsPerRow, String seatType, double price) {
        int count = 0;
        char start = fromRow.toUpperCase().charAt(0);
        char end = toRow.toUpperCase().charAt(0);
        for (char row = start; row <= end; row++) {
            for (int num = 1; num <= seatsPerRow; num++) {
                String result = addSeat(theaterId, String.valueOf(row), num, seatType, price);
                if (result != null) count++;
            }
        }
        return count;
    }

    public List<Seat> getSeatsByTheater(String theaterId) {
        return seatRepository.findByTheaterId(theaterId);
    }

    public Seat getSeatById(String seatId) {
        return seatRepository.findById(seatId);
    }

    public List<Seat> searchSeats(String theaterId, String keyword) {
        List<Seat> seats = getSeatsByTheater(theaterId);
        if (keyword == null || keyword.isBlank()) {
            return seats;
        }
        String kw = keyword.toLowerCase().trim();
        return seats.stream()
                .filter(s -> s.getSeatId().toLowerCase().contains(kw)
                        || s.getRow().toLowerCase().contains(kw)
                        || String.valueOf(s.getSeatNumber()).contains(kw)
                        || s.getStatus().toLowerCase().contains(kw)
                        || s.getSeatType().toLowerCase().contains(kw)
                        || formatLabel(s).toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    public boolean reserveSeat(String seatId) {
        Seat seat = seatRepository.findById(seatId);
        if (seat == null) return false;
        boolean result = seat.reserve();
        if (result) seatRepository.update(seat);
        return result;
    }

    public boolean releaseSeat(String seatId) {
        Seat seat = seatRepository.findById(seatId);
        if (seat == null) return false;
        boolean result = seat.release();
        if (result) seatRepository.update(seat);
        return result;
    }

    public boolean updateSeat(String seatId, String seatType, double price, String status) {
        Seat seat = seatRepository.findById(seatId);
        if (seat == null) return false;
        seat.setSeatType(seatType);
        seat.setPrice(price);
        seat.setStatus(status);
        return seatRepository.update(seat);
    }

    public boolean updateSeatFull(String seatId, String rowLabel, int seatNumber,
                                  String seatType, double price, String status) {
        Seat seat = seatRepository.findById(seatId);
        if (seat == null) return false;
        seat.setRow(rowLabel.toUpperCase());
        seat.setSeatNumber(seatNumber);
        seat.setSeatType(seatType);
        seat.setPrice(price);
        seat.setStatus(status);
        return seatRepository.update(seat);
    }

    public boolean deleteSeat(String seatId) {
        return seatRepository.delete(seatId);
    }

    private String nextSeatId() {
        int max = 0;
        for (Seat s : seatRepository.findAll()) {
            try {
                int n = Integer.parseInt(s.getSeatId().substring(1));
                if (n > max) max = n;
            } catch (Exception ignored) {
            }
        }
        return String.format("S%04d", max + 1);
    }

    private static String formatLabel(Seat s) {
        return s.getRow() + "-" + String.format("%02d", s.getSeatNumber());
    }
}
