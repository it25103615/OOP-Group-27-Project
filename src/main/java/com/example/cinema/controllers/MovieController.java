package com.example.cinema.controllers;

import com.example.cinema.models.Movie;
import com.example.cinema.services.MovieManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller // This tells Spring Boot that this class handles web traffic
public class MovieController {

    // Instantiate our manager to handle the file writing
    private final MovieManager movieManager = new MovieManager();

    // ==========================================
    // 1. SHOW THE HTML FORM
    // ==========================================
    // When a user goes to "http://localhost:8080/movies/add" in their browser...
    @GetMapping("/movies/add")
    public String showAddMovieForm(Model model) {

        model.addAttribute("movie", new Movie());

        // This tells Spring to load "add-movie-form.html" from your templates folder
        return "add-movie-form";
    }

    // ==========================================
    // 2. CATCH THE SUBMITTED DATA
    // ==========================================
    // When the user clicks the "Save Movie" button on your form...
    @PostMapping("/movies/add")
    public String saveMovie(@ModelAttribute("movie") Movie movie) {

        // Let's auto-generate a unique ID for the movie based on the current time
        movie.setMovieId("M-" + System.currentTimeMillis());

        // Save the movie to "movies.txt" using the manager we built earlier
        movieManager.addMovie(movie);

        // Redirect the user back to the form (you can change this to a success page later)
        return "redirect:/movies/add?success";
    }
}
