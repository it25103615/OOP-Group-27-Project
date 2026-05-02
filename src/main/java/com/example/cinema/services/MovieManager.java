package com.example.cinema.services;
import com.example.cinema.models.Movie;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MovieManager {

    // The text file where all movies are stored
    private static final String FILE_PATH = "movies.txt";

    //     ADD MOVIE
    public void addMovie(Movie movie) {
        // "FileWriter(FILE_PATH, true)" opens the file in "append" mode
        // so we add the new movie to the bottom of the list without erasing the old ones.
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            // Writes the movie details as a single line separated by commas
            out.println(movie.getMovieId() + "," +
                    movie.getPoster() + "," +
                    movie.getSummary() + "," +
                    movie.getShowtimes() + "," +
                    movie.getTheaterId());

            System.out.println("Success: Movie added!");

        } catch (IOException e) {
            System.out.println("Error adding movie to file: " + e.getMessage());
        }
    }

    // 2. UPDATE MOVIE

    public void updateMovie(Movie updatedMovie) {
        List<Movie> allMovies = getAllMovies(); // Get all current movies
        boolean isFound = false;

        // Loop through the list to find the movie with the matching ID
        for (int i = 0; i < allMovies.size(); i++) {
            if (allMovies.get(i).getMovieId().equals(updatedMovie.getMovieId())) {
                allMovies.set(i, updatedMovie); // Replace old details with new details
                isFound = true;
                break;
            }
        }

        // If we successfully updated it in our list, overwrite the text file with the new list
        if (isFound) {
            rewriteEntireFile(allMovies);
            System.out.println("Success: Movie updated!");
        } else {
            System.out.println("Error: Movie ID not found.");
        }
    }
}