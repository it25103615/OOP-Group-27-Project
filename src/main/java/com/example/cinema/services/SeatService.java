package com.example.cinema.services;

import com.example.cinema.models.Seat;
import com.example.cinema.util.FileHandler;

import java.util.List;

    public class SeatService {

        // CREATE single seat
        public String addSeat(String theaterId, String rowLabel,
                              int seatNumber, String seatType, double price) {
            if (theaterId == null || theaterId.isEmpty()) return null;
            if (rowLabel == null || rowLabel.isEmpty())   return null;
            if (seatNumber <= 0 || price < 0)             return null;

            // Check if seat already exists in same row and number
            for (Seat s : FileHandler.readSeatsByTheater(theaterId)) {
                if (s.getRow().equals(rowLabel) && s.getSeatNumber() == seatNumber) {
                    return null; // duplicate
                }
            }

            String id = FileHandler.generateSeatId();
            Seat seat = new Seat(id, theaterId, rowLabel.toUpperCase(), seatNumber, seatType, price);
            FileHandler.appendSeat(seat);
            return id;
        }

        // CREATE bulk seats — e.g. rows A to F, 10 seats each
        public int bulkAddSeats(String theaterId, String fromRow, String toRow,
                                int seatsPerRow, String seatType, double price) {
            int count = 0;
            char start = fromRow.toUpperCase().charAt(0);
            char end   = toRow.toUpperCase().charAt(0);
            for (char row = start; row <= end; row++) {
                for (int num = 1; num <= seatsPerRow; num++) {
                    String result = addSeat(theaterId, String.valueOf(row), num, seatType, price);
                    if (result != null) count++;
                }
            }
            return count;
        }

        // READ all seats for a theater
        public List<Seat> getSeatsByTheater(String theaterId) {
            return FileHandler.readSeatsByTheater(theaterId);
        }

        // READ by seat ID
        public Seat getSeatById(String seatId) {
            return FileHandler.findSeatById(seatId);
        }

        // UPDATE — reserve a seat
        public boolean reserveSeat(String seatId) {
            Seat seat = FileHandler.findSeatById(seatId);
            if (seat == null) return false;
            boolean result = seat.reserve(); // polymorphic call
            if (result) FileHandler.updateSeat(seat);
            return result;
        }

        // UPDATE — release a seat
        public boolean releaseSeat(String seatId) {
            Seat seat = FileHandler.findSeatById(seatId);
            if (seat == null) return false;
            boolean result = seat.release(); // polymorphic call
            if (result) FileHandler.updateSeat(seat);
            return result;
        }

        // UPDATE seat details
        public boolean updateSeat(String seatId, String seatType, double price, String status) {
            Seat seat = FileHandler.findSeatById(seatId);
            if (seat == null) return false;
            seat.setSeatType(seatType);
            seat.setPrice(price);
            seat.setStatus(status);
            return FileHandler.updateSeat(seat);
        }
        // DELETE
        public boolean deleteSeat(String seatId) {
            return FileHandler.deleteSeat(seatId);
        }
    }