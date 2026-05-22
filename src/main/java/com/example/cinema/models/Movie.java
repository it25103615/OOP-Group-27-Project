package com.example.cinema.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * DOMAIN MODEL — Movie
 * <p>
 * Represents one movie listing in the cinema catalog. Mapped to the {@code movie} table.
 * Used by the public movie list (read-only) and by admin CRUD forms (create/update/delete).
 */
@Entity
@Table(name = "movie")
public class Movie {

    @Id
    private String movieId;
    private String title;
    /** E.g. Action, Adventure, Sci-Fi */
    private String genre;
    private String poster;
    private String summary;
    /** E.g. "10:00 AM, 1:00 PM, 6:00 PM" */
    private String showtime;
    private String theaterId;

    public Movie(String movieId, String title, String genre, String poster,
                 String summary, String showtime, String theaterId) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
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

    /** Used by Thymeleaf forms and the public movie-list page. */
    public String getShowtime() {
        return showtime;
    }

    public void setShowtime(String showtime) {
        this.showtime = showtime;
    }

    /** Alias so templates may use either {@code showtime} or {@code showtimes}. */
    public String getShowtimes() {
        return showtime;
    }

    public void setShowtimes(String showtimes) {
        this.showtime = showtimes;
    }

    public String getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(String theaterId) {
        this.theaterId = theaterId;
    }

    @Override
    public String toString() {
        return movieId + "," + title + "," + genre + "," + poster + ","
                + summary + "," + showtime + "," + theaterId;
    }
}
