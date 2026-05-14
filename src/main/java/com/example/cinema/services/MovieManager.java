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
    // 3. DELETE MOVIE

    public void deleteMovie(String movieIdToDelete) {
        List<Movie> allMovies = getAllMovies(); // Get all current movies

        // Remove the movie from our Java List if the ID matches
        boolean isRemoved = allMovies.removeIf(movie -> movie.getMovieId().equals(movieIdToDelete));

        // If we removed it from the list, overwrite the text file so it is permanently gone
        if (isRemoved) {
            rewriteEntireFile(allMovies);
            System.out.println("Success: Movie deleted!");
        } else {
            System.out.println("Error: Movie ID not found.");
        }
    }


    // Reads all movies from the text file and returns them as a Java List
    public List<Movie> getAllMovies() {
        List<Movie> movieList = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return movieList;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    movieList.add(new Movie(data[0], data[1], data[2], data[3], data[4]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return movieList;
    }

    // Overwrites the entire text file (used when a movie is deleted or changed)
    private void rewriteEntireFile(List<Movie> movies) {
        // "FileWriter(FILE_PATH, false)" means OVERWRITE the file completely
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_PATH, false))) {
            for (Movie movie : movies) {
                out.println(movie.getMovieId() + "," +
                        movie.getPoster() + "," +
                        movie.getSummary() + "," +
                        movie.getShowtimes() + "," +
                        movie.getTheaterId());
            }
        } catch (IOException e) {
            System.out.println("Error updating file: " + e.getMessage());
        }
    }
}