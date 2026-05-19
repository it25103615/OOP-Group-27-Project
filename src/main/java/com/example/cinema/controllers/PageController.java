package com.example.cinema.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // ✅ Fix: forward directly instead of redirect
    @GetMapping("/")
    public String showHomePage() {
        return "forward:/index.html";
    }

    @GetMapping("/add-movie-form")
    public String showAddMovieForm() {
        return "add-movie-form";
    }
}