package com.example.cinema.services;

import com.example.cinema.models.Seat;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds sample theaters and seats into data/theaters.txt and data/seats.txt
 * when those files are empty, so the static UI has data on first run.
 * <p>Creates 7 venues with capacity 200+ and 120 seats each (A–J × 12 regular,
 * K–N × 10 VIP) so the seat map always has more than 90 seats.</p>
 */
@Component
public class TheaterSeatSeeder implements CommandLineRunner {

    private final TheaterService theaterService = new TheaterService();
    private final SeatService seatService = new SeatService();

    private static final String[][] VENUES = {
            {"Colombo Cineplex", "Colombo 03"},
            {"Majestic Cinema", "Bambalapitiya"},
            {"Liberty Lite Multiplex", "Kandy"},
            {"Scope Cinemas Negombo", "Negombo"},
            {"Regal Cinema Jaffna", "Jaffna"},
            {"Savoy Premier", "Wellawatte"},
            {"EAP Films Multiplex", "Matara"}
    };

    @Override
    public void run(String... args) {
        if (!theaterService.getAllTheaters().isEmpty()) {
            return;
        }

        for (String[] v : VENUES) {
            String id = theaterService.addTheater(v[0], v[1], 220);
            if (id == null) {
                continue;
            }
            // 10 rows × 12 = 120 regular seats (> 90 requirement)
            seatService.bulkAddSeats(id, "A", "J", 12, Seat.TYPE_REGULAR, 780.0);
            // VIP block
            seatService.bulkAddSeats(id, "K", "N", 10, Seat.TYPE_VIP, 1180.0);
            // Hold a few seats as already taken for a realistic map
            reserveOne(id, "A", 1);
            reserveOne(id, "A", 12);
            reserveOne(id, "K", 5);
        }
    }

    private void reserveOne(String theaterId, String row, int seatNumber) {
        for (Seat s : seatService.getSeatsByTheater(theaterId)) {
            if (row.equals(s.getRow()) && s.getSeatNumber() == seatNumber) {
                seatService.reserveSeat(s.getSeatId());
                break;
            }
        }
    }
}
