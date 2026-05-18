package com.example.cinema.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String showHomaPage(){
        return "index";  // show then index.html
    }

    public String showMovieDetails(){
        return "Movie-details"; // show movie details page
    }

    public String showAddMovieForm(){
        return "add-movie-form"; //show movie form page
    }


}
