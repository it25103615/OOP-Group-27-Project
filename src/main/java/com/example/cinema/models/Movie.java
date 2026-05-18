package com.example.cinema.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity  //database create for attribute
@Table(name = "movie") //uniq name table create
public class Movie {

    // Private attributes (Encapsulation)
    @Id
    private String movieId;
    private String poster; // Stores the image path or URL
    private String summary;
    private String showtimes; // E.g., "10:00 AM, 1:00 PM, 6:00 PM"
    private String theaterId;

    // Constructor to initialize the Movie object
    public Movie(String movieId, String poster, String summary, String showtimes, String theaterId) {
        this.movieId = movieId;
        this.poster = poster;
        this.summary = summary;
        this.showtimes = showtimes;
        this.theaterId = theaterId;
    }

    // Default constructor
    public Movie() {
    }

    // --- Getters and Setters ---

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getShowtimes() {
        return showtimes;
    }

    public void setShowtimes(String showtimes) {
        this.showtimes = showtimes;
    }

    public String getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(String theaterId) {
        this.theaterId = theaterId;
    }

    // Optional: Override toString() for easier debugging later
    @Override
    public String toString() {
        return movieId + "," + poster + "," + summary + "," + showtimes + "," + theaterId;
    }
}