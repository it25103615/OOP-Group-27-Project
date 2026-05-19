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

    // 1. SHOW THE HTML FORM
    // When a user goes to "http://localhost:8080/movies/add" in their browser...
    @GetMapping("/add")
    public String showAddMovieForm(Model model) {

        model.addAttribute("movie", new Movie());

        // This tells Spring to load "add-movie-form.html" from your templates folder
        return "add-movie-form";
    }

    // 2. CATCH THE SUBMITTED DATA
    // When the user clicks the "Save Movie" button on your form...
    @PostMapping("/add")
    public String saveMovie(@ModelAttribute("movie") Movie movie) {

        //Add the new movie and create uniq movie id
        String uniqueID = UUID.randomUUID().toString();
        movie.setMovieId("MOV-" + uniqueID.substring(0, 8).toUpperCase());

        // Save the movie to database
        movieRepository.save(movie);

        // Redirect the user back to the form (you can change this to a success page later)
        return "redirect:/movies/add?success";
    }

    //View all movies for database (Admin dashboard)
    @GetMapping("/all")
    public String viweAllMovies(Model model){

        model.addAttribute("movies",movieRepository.findAll());

        return "admin-dashboard";
    }

    //  Movies delete
    @PostMapping("/delete/{id}")
    public String deleteMovie(@PathVariable String id) {

        movieRepository.deleteById(id);
        return "redirect:/movies/all";
    }

    //view movie Details
    @GetMapping("/view/{id}")
    public String viewMovie(@PathVariable String id, Model model) {

        model.addAttribute("movie", movieRepository.findById(id).orElseThrow());
        return "movie-details";
    }

    // Display the form for updating Movie
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {

        model.addAttribute("movie", movieRepository.findById(id).orElseThrow());
        return "update-movie-form";
    }

    //Update movies database save!
    @PostMapping("/update/{id}")
    public String updateMovie(@PathVariable String id, @ModelAttribute Movie movie) {

        movie.setMovieId(id);  //old movie id
        movieRepository.save(movie); //Update the new data
        return "redirect:/movies/all";
    }

    // show admin dashboard
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {

        model.addAttribute("movies", movieRepository.findAll());
        return "admin-dashboard";
    }
}
