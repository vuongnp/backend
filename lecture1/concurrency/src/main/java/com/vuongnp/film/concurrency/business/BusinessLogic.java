package com.vuongnp.film.concurrency.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.vuongnp.film.concurrency.exception.InvalidDataException;
import com.vuongnp.film.concurrency.model.Movie;
import com.vuongnp.film.concurrency.model.User;
import com.vuongnp.film.concurrency.repository.RepositoryInterface;

public class BusinessLogic {
    // Genre 0 is Movie type, 1 is Series type
    private static final List<Integer> VALID_GENRES = Arrays.asList(0, 1);
    private static final List<Integer> VALID_RATING_SCORES = Arrays.asList(1, 2, 3, 4, 5);
    private RepositoryInterface repositoryInterface;

    public BusinessLogic(RepositoryInterface repositoryInterface) {
        this.repositoryInterface = repositoryInterface;
    }
    public void addMovie(Movie movie) throws Exception{
        if(!VALID_GENRES.contains(movie.getGenre())){
            throw new InvalidDataException("Invalid Movie genre! Genre must be 0 or 1");
        }
        if(repositoryInterface.hasMovie(movie.getId())){
            System.out.println("Existed movieID " + movie.getId());
            return;
        }
        repositoryInterface.addMovie(movie);
    }
    public void addToFavoriteMovies(User user, String movieID) throws Exception{
        if(!repositoryInterface.hasMovie(movieID)){
            throw new InvalidDataException("Invalid movieID: " + movieID);
        }
        repositoryInterface.addFavoriteMovie(user.getId(), movieID);
    }
    public void removeFromFavoriteMovies(User user, String movieID) throws Exception{
        if(!repositoryInterface.hasMovie(movieID)){
            throw new InvalidDataException("Invalid movieID: " + movieID);
        }
        repositoryInterface.removeFavoriteMovie(user.getId(), movieID);
    }
    public void rateMovie(User user, String movieID, int ratingScore) throws Exception{
        if(!VALID_RATING_SCORES.contains(ratingScore)){
            throw new InvalidDataException("Invalid rating score! rating score must be in [1, 2, 3, 4, 5]");
        }
        if(!repositoryInterface.hasMovie(movieID)){
            throw new InvalidDataException("Invalid movieID: " + movieID);
        }
        repositoryInterface.saveUserRating(user.getId(), movieID, ratingScore);
    }
    public List<Movie> getUserFavoriteMovies(User user) throws Exception{
        List<Movie> favoriteMovies = repositoryInterface.getUserFavoriteMovies(user.getId());
        return favoriteMovies;
    }
    public Map<Movie, Integer> getUserRatedMovies(User user){
        // Map<Movie, Integer> results = new HashMap<>();
        Map<Movie, Integer> results = new ConcurrentHashMap<>();
        Map<String, Integer> userRatedMoviesIDs = repositoryInterface.getUserRatedMovies(user.getId());
        // List<String> ratedMovieIDs = new ArrayList<>();
        List<String> ratedMovieIDs = new CopyOnWriteArrayList<>();
        for(String movieID : userRatedMoviesIDs.keySet()){
            ratedMovieIDs.add(movieID);
        }
        List<Movie> userRatedMovies = repositoryInterface.getMoviesByIDs(ratedMovieIDs);

        for(String movieID : userRatedMoviesIDs.keySet()){
            int ratingScore = userRatedMoviesIDs.get(movieID);
            Movie ratedMovie = userRatedMovies.stream().filter(movie -> movie.getId()==movieID).findFirst().get();
            results.put(ratedMovie, ratingScore);
        }
        return results;
    }
}
