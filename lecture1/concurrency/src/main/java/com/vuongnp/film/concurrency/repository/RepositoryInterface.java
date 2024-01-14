package com.vuongnp.film.concurrency.repository;

import java.util.List;
import java.util.Map;

import com.vuongnp.film.concurrency.model.Movie;

public interface RepositoryInterface {
    void addMovie(Movie movie);
    // void deleteMovie(int movie);
    boolean hasMovie(String movieID);
    List<Movie> getMoviesByIDs(List<String> movieIDs);
    void addFavoriteMovie(String userID, String movieID);
    void removeFavoriteMovie(String userID, String movieID);
    List<Movie> getUserFavoriteMovies(String userID);
    void saveUserRating(String userID, String movieID, int score);
    Map<String, Integer> getUserRatedMovies(String userID);
}
