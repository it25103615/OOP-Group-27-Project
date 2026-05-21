package com.example.cinema.services;

import com.example.cinema.models.Seat;
import com.example.cinema.util.TheaterImages;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds theaters and seats into H2 when the theaters table is empty.
 */
@Component
public class TheaterSeatSeeder implements CommandLineRunner {

    private final TheaterService theaterService;
    private final SeatService seatService;

    /** name, location, capacity */
    private static final Object[][] VENUES = {
            {"Colombo Cineplex", "Colombo 03", 320},
            {"Majestic Cinema", "Bambalapitiya", 280},
            {"Royal Grand Cinema", "Kandy", 260},
            {"Liberty Cine Hub", "Negombo", 200},
            {"Ocean View Theater", "Galle", 180},
            {"Galaxy Multiplex", "Kurunegala", 240},
            {"Platinum Screens", "Colombo 07", 350},
            {"Sapphire Cinema", "Dehiwala", 190},
            {"Empire Movie Hall", "Matara", 210},
            {"Nova Cineplex", "Jaffna", 250},
            {"Skyline Theater", "Nugegoda", 200},
            {"Metro Gold Cinema", "Wattala", 175},
            {"Pearl City Movies", "Batticaloa", 160},
            {"Regal Screen House", "Panadura", 185},
            {"Infinity Multiplex", "Moratuwa", 230},
            {"StarLight Cinema", "Trincomalee", 150},
            {"Golden Frame Theater", "Anuradhapura", 140},
            {"Liberty Lite Multiplex", "Kandy", 220},
            {"Scope Cinemas Negombo", "Negombo", 195},
            {"Savoy Premier", "Wellawatte", 205},
            {"EAP Films Multiplex", "Matara", 215},
            {"Regal Cinema Jaffna", "Jaffna", 170}
    };

    public TheaterSeatSeeder(TheaterService theaterService, SeatService seatService) {
        this.theaterService = theaterService;
        this.seatService = seatService;
    }

    @Override
    public void run(String... args) {
        if (theaterService.isEmpty()) {
            int imageIndex = 0;
            for (Object[] v : VENUES) {
                String name = (String) v[0];
                String location = (String) v[1];
                int capacity = (Integer) v[2];
                String imagePath = TheaterImages.pathForIndex(imageIndex++);
                String id = theaterService.addTheater(name, location, capacity, imagePath);
                if (id != null) {
                    seedSeatsForTheater(id, capacity);
                }
            }
        }
        // Existing H2 rows (e.g. after schema add) still get /images paths for the UI
        theaterService.backfillTheaterImages();
    }

    private void seedSeatsForTheater(String theaterId, int capacity) {
        if (capacity >= 300) {
            seatService.bulkAddSeats(theaterId, "A", "L", 14, Seat.TYPE_REGULAR, 820.0);
            seatService.bulkAddSeats(theaterId, "M", "P", 12, Seat.TYPE_VIP, 1250.0);
        } else if (capacity >= 220) {
            seatService.bulkAddSeats(theaterId, "A", "J", 12, Seat.TYPE_REGULAR, 780.0);
            seatService.bulkAddSeats(theaterId, "K", "N", 10, Seat.TYPE_VIP, 1180.0);
        } else if (capacity >= 180) {
            seatService.bulkAddSeats(theaterId, "A", "H", 11, Seat.TYPE_REGULAR, 750.0);
            seatService.bulkAddSeats(theaterId, "I", "K", 9, Seat.TYPE_VIP, 1100.0);
        } else {
            seatService.bulkAddSeats(theaterId, "A", "F", 10, Seat.TYPE_REGULAR, 700.0);
            seatService.bulkAddSeats(theaterId, "G", "J", 8, Seat.TYPE_VIP, 1050.0);
        }
        reserveOne(theaterId, "A", 1);
        reserveOne(theaterId, "A", 5);
        reserveOne(theaterId, "B", 3);
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
