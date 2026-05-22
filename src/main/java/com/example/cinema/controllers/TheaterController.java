package com.example.cinema.controllers;

import com.example.cinema.models.Booking;
import com.example.cinema.models.Seat;
import com.example.cinema.models.Theater;
import com.example.cinema.services.BookingService;
import com.example.cinema.services.SeatService;
import com.example.cinema.services.TheaterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class TheaterController {

    private final TheaterService theaterService;
    private final SeatService seatService;
    private final BookingService bookingService;

    public TheaterController(TheaterService theaterService,
                               SeatService seatService,
                               BookingService bookingService) {
        this.theaterService = theaterService;
        this.seatService = seatService;
        this.bookingService = bookingService;
    }

    // ══════════════════════════════════════════════
    //  THEATER CRUD (legacy MVC routes)
    // ══════════════════════════════════════════════

    @GetMapping("/theater/list")
    public String listTheaters(@RequestParam(required = false) String search, Model model) {
        List<Theater> theaters = (search != null && !search.trim().isEmpty())
                ? theaterService.searchByName(search)
                : theaterService.getAllTheaters();
        model.addAttribute("theaters", theaters);
        model.addAttribute("search", search);
        return "theater-list";
    }

    @GetMapping("/theater/add")
    public String showAddForm(Model model) {
        model.addAttribute("editMode", false);
        return "theater-form";
    }

    @PostMapping("/theater/save")
    public String saveTheater(@RequestParam String name,
                              @RequestParam String location,
                              @RequestParam int capacity,
                              Model model) {
        String id = theaterService.addTheater(name, location, capacity);
        if (id != null) {
            return "redirect:/theater/list?msg=Theater added successfully";
        }
        model.addAttribute("error", "Invalid input. Please check all fields.");
        return "theater-form";
    }

    @GetMapping("/theater/edit")
    public String showEditForm(@RequestParam String id, Model model) {
        Theater theater = theaterService.getTheaterById(id);
        if (theater == null) return "redirect:/theater/list?error=Theater not found";
        model.addAttribute("theater", theater);
        model.addAttribute("editMode", true);
        return "theater-form";
    }

    @PostMapping("/theater/update")
    public String updateTheater(@RequestParam String id,
                                @RequestParam String name,
                                @RequestParam String location,
                                @RequestParam int capacity) {
        boolean ok = theaterService.updateTheater(id, name, location, capacity);
        if (ok) return "redirect:/theater/list?msg=Theater updated";
        return "redirect:/theater/list?error=Update failed";
    }

    @GetMapping("/theater/delete")
    public String deleteTheater(@RequestParam String id) {
        boolean ok = theaterService.deleteTheater(id);
        if (ok) return "redirect:/theater/list?msg=Theater deleted";
        return "redirect:/theater/list?error=Delete failed";
    }

    @GetMapping("/theater/view")
    public String viewTheater(@RequestParam String id, Model model) {
        Theater theater = theaterService.getTheaterById(id);
        if (theater == null) return "redirect:/theater/list?error=Not found";
        model.addAttribute("theater", theater);
        model.addAttribute("availableSeats", theaterService.getAvailableSeats(id));
        model.addAttribute("reservedSeats", theaterService.getReservedSeats(id));
        return "theater-view";
    }

    // ══════════════════════════════════════════════
    //  SEAT CRUD (legacy MVC routes)
    // ══════════════════════════════════════════════

    @GetMapping("/seat/map")
    public String seatMap(@RequestParam String theaterId, Model model) {
        List<Seat> seats = seatService.getSeatsByTheater(theaterId);
        Map<String, List<Seat>> byRow = new TreeMap<>();
        for (Seat s : seats) {
            byRow.computeIfAbsent(s.getRow(), k -> new ArrayList<>()).add(s);
        }
        model.addAttribute("theater", theaterService.getTheaterById(theaterId));
        model.addAttribute("seats", seats);
        model.addAttribute("byRow", byRow);
        model.addAttribute("theaterId", theaterId);
        return "seat-map";
    }

    @GetMapping("/seat/add")
    public String showSeatForm(@RequestParam String theaterId, Model model) {
        model.addAttribute("theaterId", theaterId);
        model.addAttribute("theater", theaterService.getTheaterById(theaterId));
        model.addAttribute("editMode", false);
        return "seat-form";
    }

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

    @GetMapping("/seat/edit")
    public String showSeatEditForm(@RequestParam String seatId, Model model) {
        Seat seat = seatService.getSeatById(seatId);
        if (seat == null) return "redirect:/theater/list?error=Seat not found";
        model.addAttribute("seat", seat);
        model.addAttribute("theater", theaterService.getTheaterById(seat.getTheaterId()));
        model.addAttribute("editMode", true);
        return "seat-form";
    }

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

    @GetMapping("/seat/delete")
    public String deleteSeat(@RequestParam String seatId, @RequestParam String theaterId) {
        seatService.deleteSeat(seatId);
        return "redirect:/seat/map?theaterId=" + theaterId + "&msg=Seat deleted";
    }

    // ══════════════════════════════════════════════
    //  REST — public UI (theaters.html, seats.html, payment)
    // ══════════════════════════════════════════════

    @GetMapping("/api/theaters")
    @ResponseBody
    public List<Map<String, Object>> apiListTheaters(@RequestParam(required = false) String search) {
        List<Theater> theaters = (search != null && !search.trim().isEmpty())
                ? theaterService.searchByName(search)
                : theaterService.getAllTheaters();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Theater t : theaters) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", t.getId());
            row.put("name", t.getName());
            row.put("location", t.getLocation());
            row.put("capacity", t.getCapacity());
            row.put("availableSeats", theaterService.getAvailableSeats(t.getId()));
            row.put("reservedSeats", theaterService.getReservedSeats(t.getId()));
            result.add(row);
        }
        return result;
    }

    @GetMapping("/api/theaters/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> apiGetTheater(@PathVariable String id) {
        Theater theater = theaterService.getTheaterById(id);
        if (theater == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String, Object> body = new HashMap<>();
        body.put("id", theater.getId());
        body.put("name", theater.getName());
        body.put("location", theater.getLocation());
        body.put("capacity", theater.getCapacity());
        body.put("availableSeats", theaterService.getAvailableSeats(id));
        body.put("reservedSeats", theaterService.getReservedSeats(id));
        return ResponseEntity.ok(body);
    }

    @GetMapping("/api/seats")
    @ResponseBody
    public ResponseEntity<List<Seat>> apiGetSeats(@RequestParam String theaterId) {
        if (theaterService.getTheaterById(theaterId) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(seatService.getSeatsByTheater(theaterId));
    }

    @PostMapping("/api/seats/{seatId}/reserve")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> apiReserveSeat(@PathVariable String seatId) {
        Seat seat = seatService.getSeatById(seatId);
        if (seat == null) {
            return ResponseEntity.notFound().build();
        }
        boolean ok = seat.isAvailable()
                ? seatService.reserveSeat(seatId)
                : seatService.releaseSeat(seatId);
        Seat updated = seatService.getSeatById(seatId);
        Map<String, Object> body = new HashMap<>();
        body.put("success", ok);
        body.put("status", updated != null ? updated.getStatus() : null);
        return ok ? ResponseEntity.ok(body) : ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @PostMapping("/api/seats/{seatId}/book")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> apiBookSeat(@PathVariable String seatId) {
        Seat seat = seatService.getSeatById(seatId);
        if (seat == null) {
            return ResponseEntity.notFound().build();
        }
        boolean ok = seatService.reserveSeat(seatId);
        Seat updated = seatService.getSeatById(seatId);
        Map<String, Object> body = new HashMap<>();
        body.put("success", ok);
        body.put("status", updated != null ? updated.getStatus() : null);
        return ok ? ResponseEntity.ok(body) : ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    /** POST /api/bookings/confirm — after payment; saves booking to H2 and reserves seats */
    @PostMapping("/api/bookings/confirm")
    @ResponseBody
    public ResponseEntity<?> confirmBooking(@RequestBody Map<String, Object> payload) {
        try {
            Booking b = bookingService.confirmBooking(payload);
            return ResponseEntity.ok(b);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
        }
    }

    @GetMapping("/api/bookings/{id}")
    @ResponseBody
    public ResponseEntity<Booking> getBooking(@PathVariable String id) {
        Booking b = bookingService.getById(id);
        return b != null ? ResponseEntity.ok(b) : ResponseEntity.notFound().build();
    }

    // ══════════════════════════════════════════════
    //  REST — admin CRUD (theater-admin.html)
    // ══════════════════════════════════════════════

    @GetMapping("/api/admin/theaters")
    @ResponseBody
    public List<Map<String, Object>> adminListTheaters(@RequestParam(required = false) String search) {
        return apiListTheaters(search);
    }

    @PostMapping("/api/admin/theaters")
    @ResponseBody
    public ResponseEntity<?> adminAddTheater(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        String location = (String) body.get("location");
        int capacity = body.get("capacity") instanceof Number
                ? ((Number) body.get("capacity")).intValue() : 0;
        String id = theaterService.addTheater(name, location, capacity);
        if (id == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid theater data"));
        }
        return ResponseEntity.ok(Map.of("id", id));
    }

    @PutMapping("/api/admin/theaters/{id}")
    @ResponseBody
    public ResponseEntity<?> adminUpdateTheater(@PathVariable String id, @RequestBody Map<String, Object> body) {
        boolean ok = theaterService.updateTheater(id,
                (String) body.get("name"),
                (String) body.get("location"),
                ((Number) body.get("capacity")).intValue());
        return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/api/admin/theaters/{id}")
    @ResponseBody
    public ResponseEntity<?> adminDeleteTheater(@PathVariable String id) {
        return theaterService.deleteTheater(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/api/admin/seats")
    @ResponseBody
    public List<Seat> adminListSeats(@RequestParam String theaterId,
                                     @RequestParam(required = false) String search) {
        return seatService.searchSeats(theaterId, search);
    }

    @PostMapping("/api/admin/seats")
    @ResponseBody
    public ResponseEntity<?> adminAddSeat(@RequestBody Map<String, Object> body) {
        String theaterId = (String) body.get("theaterId");
        String row = (String) body.get("row");
        int num = ((Number) body.get("seatNumber")).intValue();
        String type = (String) body.get("seatType");
        double price = ((Number) body.get("price")).doubleValue();
        String id = seatService.addSeat(theaterId, row, num, type, price);
        if (id == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Seat exists or invalid"));
        }
        return ResponseEntity.ok(Map.of("seatId", id));
    }

    @PutMapping("/api/admin/seats/{seatId}")
    @ResponseBody
    public ResponseEntity<?> adminUpdateSeat(@PathVariable String seatId, @RequestBody Map<String, Object> body) {
        boolean ok = seatService.updateSeatFull(seatId,
                (String) body.get("row"),
                ((Number) body.get("seatNumber")).intValue(),
                (String) body.get("seatType"),
                ((Number) body.get("price")).doubleValue(),
                (String) body.get("status"));
        return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/api/admin/seats/{seatId}")
    @ResponseBody
    public ResponseEntity<?> adminDeleteSeat(@PathVariable String seatId) {
        return seatService.deleteSeat(seatId)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
