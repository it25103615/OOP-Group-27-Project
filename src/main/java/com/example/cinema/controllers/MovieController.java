package com.example.cinema.controllers;

import com.example.cinema.models.Movie;
import com.example.cinema.services.MovieManager;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

/**
 * WEB LAYER — MovieController
 * <p>
 * Maps movie URLs to Thymeleaf pages and delegates business logic to {@link MovieManager}.
 * See routing table in project docs / README from the Movie module owner.
 */
@Controller
@RequestMapping("/movies")
public class MovieController {

    /** Session key set by {@link #syncSession(Map, HttpSession)} after admin client login. */
    public static final String SESSION_USER_TYPE = "userType";

    @Autowired
    private MovieManager movieManager;

    // 1. SHOW THE HTML FORM
    // When a user goes to "http://localhost:8080/movies/add" in their browser...
    @GetMapping("/list")
    public String showMovieList(Model model) {
        model.addAttribute("movies", movieManager.getAvailableMovies());
        return "movie-list";
    }

    /**
     * JSON API for static/index.html to load real DB movies (public read).
     */
    @GetMapping("/api/available")
    @ResponseBody
    public List<Movie> getAvailableMoviesApi() {
        return movieManager.getAvailableMovies();
    }

    /**
     * Movie details page → movie-details.html
     */
    @GetMapping("/view/{id}")
    public String viewMovie(@PathVariable String id, Model model) {
        Movie movie = movieManager.getMovieById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found: " + id));
        model.addAttribute("movie", movie);
        return "movie-details";
    }

    // ===================== ADMIN (MovieManager verifies Admin role) =====================

    /**
     * Admin CRUD hub → admin-dashboard.html
     */
    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("movies", movieManager.getAllMoviesForAdmin(resolveUserType(session)));
            return "admin-dashboard";
        } catch (SecurityException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/movies/list";
        }
    }

    /** Alias for /dashboard */
    @GetMapping("/all")
    public String viewAllMoviesAlias(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        return showDashboard(model, session, redirectAttributes);
    }

    /** Add form → add-movie-form.html */
    @GetMapping("/add")
    public String showAddMovieForm(Model model) {

        model.addAttribute("movie", new Movie());

        // This tells Spring to load "add-movie-form.html" from your templates folder
        return "add-movie-form";
    }

    // 2. CATCH THE SUBMITTED DATA
    // When the user clicks the "Save Movie" button on your form...
    @PostMapping("/add")
    public String saveMovie(@ModelAttribute("movie") Movie movie,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        try {
            movieManager.addMovie(movie, resolveUserType(session));
            redirectAttributes.addAttribute("success", true);
            return "redirect:/movies/add";
        } catch (SecurityException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/movies/list";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/movies/add";
        }
    }

    // Display the form for updating Movie
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Movie movie = movieManager.getMovieById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found: " + id));
        model.addAttribute("movie", movie);
        return "update-movie-form";
    }

    //Update movies database save!
    @PostMapping("/update/{id}")
    public String updateMovie(@PathVariable String id,
                              @ModelAttribute("movie") Movie movie,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        try {
            movieManager.updateMovie(id, movie, resolveUserType(session));
            redirectAttributes.addFlashAttribute("success", "Movie updated successfully.");
            return "redirect:/movies/dashboard";
        } catch (SecurityException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/movies/list";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/movies/edit/" + id;
        }
    }

    /** Delete movie (POST) */
    @PostMapping("/delete/{id}")
    public String deleteMovie(@PathVariable String id,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        try {
            movieManager.deleteMovie(id, resolveUserType(session));
            redirectAttributes.addFlashAttribute("success", "Movie deleted successfully.");
        } catch (SecurityException | IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/movies/dashboard";
    }

    /**
     * Syncs server session with client AuthService role so POST mutations pass admin checks.
     */
    @PostMapping("/session/sync")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> syncSession(@RequestBody Map<String, String> body,
                                                           HttpSession session) {
        String userType = body.get("userType");
        if (userType == null || userType.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "error", "userType is required."));
        }
        session.setAttribute(SESSION_USER_TYPE, userType);
        return ResponseEntity.ok(Map.of("ok", true, "userType", userType));
    }

    private String resolveUserType(HttpSession session) {
        Object type = session.getAttribute(SESSION_USER_TYPE);
        return type != null ? type.toString() : "Customer";
    }
}
