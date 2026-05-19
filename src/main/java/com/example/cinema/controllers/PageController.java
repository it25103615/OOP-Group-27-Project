package com.example.cinema.controllers;

import com.example.cinema.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @Autowired
    private MovieRepository movieRepository;

    // data base store movies send index.html
    @GetMapping("/")
    public String showHomePage(Model model){
        model.addAttribute("movies",movieRepository.findAll());
        return "index";
    }
//    @GetMapping("/details")
//    public String showMovieDetails(){
//        return "Movie-details"; // show movie details page
//    }
    @GetMapping("/add-movie-form")
    public String showAddMovieForm(){
        return "add-movie-form"; //show movie form page
    }


}
