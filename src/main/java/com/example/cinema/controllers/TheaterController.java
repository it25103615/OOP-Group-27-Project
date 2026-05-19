package com.example.cinema.controllers;
import com.example.cinema.models.Seat;
import com.example.cinema.models.Theater;
import com.example.cinema.services.SeatService;
import com.example.cinema.services.TheaterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

    // @Controller tells Spring Boot this class handles web requests
// It replaces the old HttpServlet approach
    @Controller
    public class TheaterController {

        // Service objects — handle all business logic
        private final TheaterService theaterService = new TheaterService();
        private final SeatService    seatService    = new SeatService();

        // ══════════════════════════════════════════════
        //  THEATER CRUD
        // ══════════════════════════════════════════════

        // READ — show all theaters
        // URL: http://localhost:8080/theater/list
        @GetMapping("/theater/list")
        public String listTheaters(@RequestParam(required = false) String search,
                                   Model model) {
            List<Theater> theaters;
            if (search != null && !search.trim().isEmpty()) {
                theaters = theaterService.searchByName(search);
            } else {
                theaters = theaterService.getAllTheaters();
            }
            model.addAttribute("theaters", theaters);
            model.addAttribute("search", search);
            return "theater-list"; // goes to WEB-INF/views/theater-list.jsp
        }

        // READ — show add theater form
        // URL: http://localhost:8080/theater/add
        @GetMapping("/theater/add")
        public String showAddForm(Model model) {
            model.addAttribute("editMode", false);
            return "theater-form"; // goes to WEB-INF/views/theater-form.jsp
        }

        // CREATE — save new theater from form
        // URL: POST http://localhost:8080/theater/save
        @PostMapping("/theater/save")
        public String saveTheater(@RequestParam String name,
                                  @RequestParam String location,
                                  @RequestParam int capacity,
                                  @RequestParam int screenCount,
                                  @RequestParam(defaultValue = "false") boolean hasAC,
                                  @RequestParam String theaterType,
                                  Model model) {
            String id = theaterService.addTheater(name, location, capacity);
            if (id != null) {
                return "redirect:/theater/list?msg=Theater added successfully";
            }
            model.addAttribute("error", "Invalid input. Please check all fields.");
            return "theater-form";
        }

        // READ — show edit form with existing data
        // URL: http://localhost:8080/theater/edit?id=T001
        @GetMapping("/theater/edit")
        public String showEditForm(@RequestParam String id, Model model) {
            Theater theater = theaterService.getTheaterById(id);
            if (theater == null) return "redirect:/theater/list?error=Theater not found";
            model.addAttribute("theater", theater);
            model.addAttribute("editMode", true);
            return "theater-form";
        }

        // UPDATE — save edited theater
        // URL: POST http://localhost:8080/theater/update
        @PostMapping("/theater/update")
        public String updateTheater(@RequestParam String id,
                                    @RequestParam String name,
                                    @RequestParam String location,
                                    @RequestParam int capacity,
                                    @RequestParam int screenCount,
                                    @RequestParam(defaultValue = "false") boolean hasAC,
                                    @RequestParam String theaterType) {
            boolean ok = theaterService.updateTheater(id, name, location,
                    capacity, screenCount, hasAC, theaterType);
            if (ok) return "redirect:/theater/list?msg=Theater updated";
            return "redirect:/theater/list?error=Update failed";
        }

        // DELETE — remove theater and all its seats
        // URL: http://localhost:8080/theater/delete?id=T001
        @GetMapping("/theater/delete")
        public String deleteTheater(@RequestParam String id) {
            boolean ok = theaterService.deleteTheater(id);
            if (ok) return "redirect:/theater/list?msg=Theater deleted";
            return "redirect:/theater/list?error=Delete failed";
        }

        // READ — view theater details
        // URL: http://localhost:8080/theater/view?id=T001
        @GetMapping("/theater/view")
        public String viewTheater(@RequestParam String id, Model model) {
            Theater theater = theaterService.getTheaterById(id);
            if (theater == null) return "redirect:/theater/list?error=Not found";
            model.addAttribute("theater", theater);
            model.addAttribute("availableSeats", theaterService.getAvailableSeats(id));
            model.addAttribute("reservedSeats",  theaterService.getReservedSeats(id));
            return "theater-view";
        }

        // ══════════════════════════════════════════════
        //  SEAT CRUD
        // ══════════════════════════════════════════════

        // READ — show seat map for a theater
        // URL: http://localhost:8080/seat/map?theaterId=T001
        @GetMapping("/seat/map")
        public String seatMap(@RequestParam String theaterId, Model model) {
            List<Seat> seats = seatService.getSeatsByTheater(theaterId);

            // Group seats by row for the grid display
            Map<String, List<Seat>> byRow = new TreeMap<>();
            for (Seat s : seats) {
                byRow.computeIfAbsent(s.getRow(), k -> new java.util.ArrayList<>()).add(s);
            }

            model.addAttribute("theater",  theaterService.getTheaterById(theaterId));
            model.addAttribute("seats",    seats);
            model.addAttribute("byRow",    byRow);
            model.addAttribute("theaterId", theaterId);
            return "seat-map";
        }

        // READ — show add seat form
        @GetMapping("/seat/add")
        public String showSeatForm(@RequestParam String theaterId, Model model) {
            model.addAttribute("theaterId", theaterId);
            model.addAttribute("theater",   theaterService.getTheaterById(theaterId));
            model.addAttribute("editMode",  false);
            return "seat-form";
        }

        // CREATE — save new seat
        @PostMapping("/seat/save")
        public String saveSeat(@RequestParam String theaterId,
                               @RequestParam String rowLabel,
                               @RequestParam int seatNumber,
                               @RequestParam String seatType,
                               @RequestParam double price) {
            String id = seatService.addSeat(theaterId, rowLabel, seatNumber, seatType, price);
            if (id != null) return "redirect:/seat/map?theaterId=" + theaterId + "&msg=Seat added";
            return "redirect:/seat/add?theaterId=" + theaterId + "&error=Seat already exists";
        }

        // CREATE — bulk add seats
        @PostMapping("/seat/bulk")
        public String bulkAddSeats(@RequestParam String theaterId,
                                   @RequestParam String fromRow,
                                   @RequestParam String toRow,
                                   @RequestParam int seatsPerRow,
                                   @RequestParam String seatType,
                                   @RequestParam double price) {
            int count = seatService.bulkAddSeats(theaterId, fromRow, toRow, seatsPerRow, seatType, price);
            return "redirect:/seat/map?theaterId=" + theaterId + "&msg=" + count + " seats added";
        }

        // READ — show edit seat form
        @GetMapping("/seat/edit")
        public String showSeatEditForm(@RequestParam String seatId, Model model) {
            Seat seat = seatService.getSeatById(seatId);
            if (seat == null) return "redirect:/theater/list?error=Seat not found";
            model.addAttribute("seat",     seat);
            model.addAttribute("theater",  theaterService.getTheaterById(seat.getTheaterId()));
            model.addAttribute("editMode", true);
            return "seat-form";
        }

        // UPDATE — save seat changes
        @PostMapping("/seat/update")
        public String updateSeat(@RequestParam String seatId,
                                 @RequestParam String seatType,
                                 @RequestParam double price,
                                 @RequestParam String status,
                                 @RequestParam String theaterId) {
            boolean ok = seatService.updateSeat(seatId, seatType, price, status);
            if (ok) return "redirect:/seat/map?theaterId=" + theaterId + "&msg=Seat updated";
            return "redirect:/seat/edit?seatId=" + seatId + "&error=Update failed";
        }

        // UPDATE — reserve a seat
        @PostMapping("/seat/reserve")
        public String reserveSeat(@RequestParam String seatId,
                                  @RequestParam String theaterId,
                                  @RequestParam String reserveAction) {
            if ("reserve".equals(reserveAction)) {
                seatService.reserveSeat(seatId);
            } else {
                seatService.releaseSeat(seatId);
            }
            return "redirect:/seat/map?theaterId=" + theaterId + "&msg=Status updated";
        }

        // DELETE — remove a seat
        @GetMapping("/seat/delete")
        public String deleteSeat(@RequestParam String seatId,
                                 @RequestParam String theaterId) {
            seatService.deleteSeat(seatId);
            return "redirect:/seat/map?theaterId=" + theaterId + "&msg=Seat deleted";
        }
    }