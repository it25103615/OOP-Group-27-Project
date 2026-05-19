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

    // database store movies send index.html
    // "http://localhost:8080/" showing index.html file
    @GetMapping("/")
    public String showHomePage(Model model){

        model.addAttribute("movies",movieRepository.findAll());
        return "index";
    }
}
