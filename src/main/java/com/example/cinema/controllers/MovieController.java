package com.example.cinema.controllers;

import com.example.cinema.models.Movie;
import com.example.cinema.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller // This tells Spring Boot that this class handles web traffic
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;
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

        if (movie.getMovieId() == null || movie.getMovieId().isEmpty()) {
            movie.setMovieId("MOV-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase());
        }

        // Save the movie to data base
        movieRepository.save(movie);

        // Redirect the user back to the form (you can change this to a success page later)
        return "redirect:/movies/add?success";
    }
    @GetMapping("/movies/all")
    public String viweAllMovies(Model model){
        model.addAttribute("movies",movieRepository.findAll());

        return "movie-list";
    }
    @PostMapping("/delete/{id}")
    public String deleteMovie(@PathVariable String id) {
        movieRepository.deleteById(id);
        return "redirect:/movies/all";
    }
}
