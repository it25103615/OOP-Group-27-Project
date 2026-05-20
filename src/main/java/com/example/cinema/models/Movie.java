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
    private String title;
    private String poster; // Stores the image path or URL
    private String summary;
    private String showtime; // E.g., "10:00 AM, 1:00 PM, 6:00 PM"
    private String theaterId;

    // Constructor to initialize the Movie object
    public Movie(String movieId,String title , String poster, String summary, String showtime, String theaterId) {
        this.movieId = movieId;
        this.title = title;
        this.poster = poster;
        this.summary = summary;
        this.showtime = showtime;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    /** Primary accessor used by forms and public movie pages. */
    public String getShowtime() {
        return showtime;
    }

    public void setShowtime(String showtime) {
        this.showtime = showtime;
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
        return movieId + "," + poster + "," + summary + "," + showtime + "," + theaterId;
    }
}
