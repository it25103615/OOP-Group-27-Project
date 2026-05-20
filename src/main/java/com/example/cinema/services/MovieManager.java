package com.example.cinema.services;

import com.example.cinema.models.Movie;
import com.example.cinema.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieManager {

    /** Must match the {@code type} value returned by {@code /api/users/login} for admins. */
    public static final String ADMIN_ROLE = "Admin";

    @Autowired
    private MovieRepository movieRepository;

    /**
     * PUBLIC — any visitor or Customer may call this (no admin check).
     * Returns every movie currently stored in the catalog for browsing/buying tickets.
     */
    public List<Movie> getAvailableMovies() {
        return movieRepository.findAll();
    }

    /**
     * PUBLIC — fetch one movie by ID for the details page.
     */
    public Optional<Movie> getMovieById(String movieId) {
        if (movieId == null || movieId.isBlank()) {
            return Optional.empty();
        }
        return movieRepository.findById(movieId);
    }

    /**
     * ADMIN ONLY — creates a new movie with a generated MOV-xxxxxxxx ID.
     *
     * @param userType role from HTTP session (set after login); must be {@link #ADMIN_ROLE}
     */
    public Movie addMovie(Movie movie, String userType) {
        verifyAdminAccess(userType);
        if (movie == null) {
            throw new IllegalArgumentException("Movie cannot be null.");
        }
        validateMovieFields(movie, true);

        String uniqueId = UUID.randomUUID().toString();
        movie.setMovieId("MOV-" + uniqueId.substring(0, 8).toUpperCase());

        return movieRepository.save(movie);
    }

    /**
     * ADMIN ONLY — updates an existing movie row.
     */
    public Movie updateMovie(String movieId, Movie updatedMovie, String userType) {
        verifyAdminAccess(userType);
        if (movieId == null || movieId.isBlank()) {
            throw new IllegalArgumentException("Movie ID is required.");
        }
        if (!movieRepository.existsById(movieId)) {
            throw new IllegalArgumentException("Movie not found: " + movieId);
        }
        if (updatedMovie == null) {
            throw new IllegalArgumentException("Updated movie data cannot be null.");
        }

        validateMovieFields(updatedMovie, false);
        updatedMovie.setMovieId(movieId);
        return movieRepository.save(updatedMovie);
    }

    /**
     * ADMIN ONLY — removes a movie from the catalog.
     */
    public void deleteMovie(String movieId, String userType) {
        verifyAdminAccess(userType);
        if (movieId == null || movieId.isBlank()) {
            throw new IllegalArgumentException("Movie ID is required.");
        }
        if (!movieRepository.existsById(movieId)) {
            throw new IllegalArgumentException("Movie not found: " + movieId);
        }
        movieRepository.deleteById(movieId);
    }

    /**
     * ADMIN ONLY — used by the admin dashboard to list movies before edit/delete.
     * Same data as {@link #getAvailableMovies()} but documents that admins use the dashboard route.
     */
    public List<Movie> getAllMoviesForAdmin(String userType) {
        verifyAdminAccess(userType);
        return movieRepository.findAll();
    }

    /**
     * Ensures only Admin users can mutate the catalog.
     * The controller supplies {@code userType} from the server HTTP session after
     * {@code /movies/session/sync} is called from an authenticated admin page.
     */
    private void verifyAdminAccess(String userType) {
        if (userType == null || !ADMIN_ROLE.equalsIgnoreCase(userType.trim())) {
            throw new SecurityException("Access denied: Admin privileges are required for this operation.");
        }
    }

    /** Shared validation for add and update. */
    private void validateMovieFields(Movie movie, boolean isNew) {
        if (movie.getTitle() == null || movie.getTitle().isBlank()) {
            throw new IllegalArgumentException("Movie title is required.");
        }
        if (movie.getGenre() == null || movie.getGenre().isBlank()) {
            throw new IllegalArgumentException("Genre is required.");
        }
        if (movie.getPoster() == null || movie.getPoster().isBlank()) {
            throw new IllegalArgumentException("Poster URL is required.");
        }
        if (movie.getSummary() == null || movie.getSummary().isBlank()) {
            throw new IllegalArgumentException("Movie summary is required.");
        }
        if (movie.getShowtime() == null || movie.getShowtime().isBlank()) {
            throw new IllegalArgumentException("Showtimes are required.");
        }
        if (movie.getTheaterId() == null || movie.getTheaterId().isBlank()) {
            throw new IllegalArgumentException("Theater ID is required.");
        }
        if (isNew && movie.getMovieId() != null && !movie.getMovieId().isBlank()) {
            movie.setMovieId(null);
        }
    }
}
