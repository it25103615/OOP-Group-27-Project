package com.example.cinema.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    /** Homepage lives in src/main/resources/static/index.html (not templates/). */
    @GetMapping({"/", "/index"})
    public String showHomePage() {
        return "redirect:/index.html";
    }
    @GetMapping("/details")
    public String showMovieDetails(){
        return "Movie-details"; // show movie details page
    }
    @GetMapping("/add-movie-form")
    public String showAddMovieForm(){
        return "add-movie-form"; //show movie form page
    }


}
