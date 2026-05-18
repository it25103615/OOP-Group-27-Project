package com.example.cinema.util;

import com.example.cinema.models.Seat;
import com.example.cinema.models.Theater;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

    // Handles all file read/write for theaters.txt and seats.txt
    public class FileHandler {

        private static final String THEATERS_FILE = "data/theaters.txt";
        private static final String SEATS_FILE    = "data/seats.txt";

        // Create data folder and empty files if they don't exist
        static {
            new File("data").mkdirs();
            createIfMissing(THEATERS_FILE);
            createIfMissing(SEATS_FILE);
        }

        private static void createIfMissing(String path) {
            try { new File(path).createNewFile(); }
            catch (IOException e) { System.err.println("Cannot create: " + path); }
        }

        // ── THEATER: READ ──────────────────────────────────────────────────────

        public static List<Theater> readAllTheaters() {
            List<Theater> list = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(THEATERS_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty() && !line.startsWith("#")) {
                        Theater t = Theater.fromCsvLine(line);
                        if (t != null) list.add(t);
                    }
                }
            } catch (IOException e) { System.err.println("Read error: " + e.getMessage()); }
            return list;
        }

        public static Theater findTheaterById(String id) {
            for (Theater t : readAllTheaters())
                if (t.getId().equals(id)) return t;
            return null;
        }

        // ── THEATER: WRITE ─────────────────────────────────────────────────────

        // Append one theater (used for CREATE — no need to rewrite the whole file)
        public static void appendTheater(Theater t) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(THEATERS_FILE, true))) {
                bw.write(t.toCsvLine()); bw.newLine();
            } catch (IOException e) { System.err.println("Write error: " + e.getMessage()); }
        }

        // Overwrite the whole file (used for UPDATE and DELETE)
        public static void writeAllTheaters(List<Theater> list) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(THEATERS_FILE, false))) {
                for (Theater t : list) { bw.write(t.toCsvLine()); bw.newLine(); }
            } catch (IOException e) { System.err.println("Write error: " + e.getMessage()); }
        }

        public static boolean updateTheater(Theater updated) {
            List<Theater> list = readAllTheaters();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId().equals(updated.getId())) {
                    list.set(i, updated);
                    writeAllTheaters(list);
                    return true;
                }
            }
            return false;
        }

        public static boolean deleteTheater(String id) {
            List<Theater> list = readAllTheaters();
            boolean removed = list.removeIf(t -> t.getId().equals(id));
            if (removed) {
                writeAllTheaters(list);
                deleteSeatsForTheater(id); // also remove all seats of this theater
            }
            return removed;
        }

        // Generate next ID: T001, T002, ...
        public static String generateTheaterId() {
            int max = 0;
            for (Theater t : readAllTheaters()) {
                try { int n = Integer.parseInt(t.getId().substring(1)); if (n > max) max = n; }
                catch (NumberFormatException ignored) {}
            }
            return String.format("T%03d", max + 1);
        }

        // ── SEAT: READ ─────────────────────────────────────────────────────────

        public static List<Seat> readAllSeats() {
            List<Seat> list = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(SEATS_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty() && !line.startsWith("#")) {
                        Seat s = Seat.fromCsvLine(line);
                        if (s != null) list.add(s);
                    }
                }
            } catch (IOException e) { System.err.println("Read error: " + e.getMessage()); }
            return list;
        }

        public static List<Seat> readSeatsByTheater(String theaterId) {
            List<Seat> list = new ArrayList<>();
            for (Seat s : readAllSeats())
                if (s.getTheaterId().equals(theaterId)) list.add(s);
            return list;
        }

        public static Seat findSeatById(String seatId) {
            for (Seat s : readAllSeats())
                if (s.getSeatId().equals(seatId)) return s;
            return null;
        }

        // ── SEAT: WRITE ────────────────────────────────────────────────────────

        public static void appendSeat(Seat s) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(SEATS_FILE, true))) {
                bw.write(s.toCsvLine()); bw.newLine();
            } catch (IOException e) { System.err.println("Write error: " + e.getMessage()); }
        }

        public static void writeAllSeats(List<Seat> list) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(SEATS_FILE, false))) {
                for (Seat s : list) { bw.write(s.toCsvLine()); bw.newLine(); }
            } catch (IOException e) { System.err.println("Write error: " + e.getMessage()); }
        }

        public static boolean updateSeat(Seat updated) {
            List<Seat> list = readAllSeats();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getSeatId().equals(updated.getSeatId())) {
                    list.set(i, updated);
                    writeAllSeats(list);
                    return true;
                }
            }
            return false;
        }

        public static boolean deleteSeat(String seatId) {
            List<Seat> list = readAllSeats();
            boolean removed = list.removeIf(s -> s.getSeatId().equals(seatId));
            if (removed) writeAllSeats(list);
            return removed;
        }

        public static void deleteSeatsForTheater(String theaterId) {
            List<Seat> list = readAllSeats();
            list.removeIf(s -> s.getTheaterId().equals(theaterId));
            writeAllSeats(list);
        }

        public static String generateSeatId() {
            int max = 0;
            for (Seat s : readAllSeats()) {
                try { int n = Integer.parseInt(s.getSeatId().substring(1)); if (n > max) max = n; }
                catch (NumberFormatException ignored) {}
            }
            return String.format("S%04d", max + 1);
        }

        // Stats helpers
        public static long countAvailable(String theaterId) {
            return readSeatsByTheater(theaterId).stream().filter(Seat::isAvailable).count();
        }

        public static long countReserved(String theaterId) {
            return readSeatsByTheater(theaterId).stream()
                    .filter(s -> Seat.STATUS_RESERVED.equals(s.getStatus())).count();
        }
    }

