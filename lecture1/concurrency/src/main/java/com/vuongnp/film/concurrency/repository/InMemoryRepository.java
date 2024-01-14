package com.vuongnp.film.concurrency.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.vuongnp.film.concurrency.model.Movie;

public class InMemoryRepository implements RepositoryInterface{
    // private static final Map<String, Movie> movieMap = new HashMap<>();
    private static final Map<String, Movie> movieMap = new ConcurrentHashMap<>();
    // private static final Map<String, Set<String>> userFavoriteMap = new ConcurrentHashMap<>();
    private static final Map<String, List<String>> userFavoriteMap = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, Integer>> userRatingMap = new ConcurrentHashMap<>();

    @Override
    public void addMovie(Movie movie) {
        movieMap.put(movie.getId(), movie);
    }

    @Override
    public boolean hasMovie(String movieID) {
        return movieMap.containsKey(movieID);
    }

    @Override
    public void addFavoriteMovie(String userID, String movieID) {
       if(!userFavoriteMap.containsKey(userID)){
            // userFavoriteMap.put(userID, new HashSet<>());
            userFavoriteMap.put(userID, new CopyOnWriteArrayList<>());
        }else{
            if(userFavoriteMap.get(userID).contains(movieID)){
                System.out.println("MovieId " + movieID + " was existed in the favorite list of userId " + userID);
            }else{
                userFavoriteMap.get(userID).add(movieID);
            }
        }
        
    }

    @Override
    public void removeFavoriteMovie(String userID, String movieID) {
       if(!userFavoriteMap.containsKey(userID)){
            throw new RuntimeException("Cannot find userID: " + userID);
       }else{
            if(!userFavoriteMap.get(userID).contains(movieID)){
                throw new RuntimeException(movieID + " is not in favorite movies of UserID " + userID);
            }else{
                userFavoriteMap.get(userID).remove(movieID);
            }
       }
    }

    @Override
    public List<Movie> getMoviesByIDs(List<String> movieIDs) {
        // List<Movie> movies = new ArrayList<>();
        List<Movie> movies = new CopyOnWriteArrayList<>();
        for(String movieId : movieIDs){
            if(!hasMovie(movieId)){
                throw new RuntimeException("Cannot find movieID: " + movieId);
            }
            Movie movie = movieMap.get(movieId);
            movies.add(movie);
        }
        return movies;
    }

    @Override
    public List<Movie> getUserFavoriteMovies(String userID) {
        if(!userFavoriteMap.containsKey(userID)){
            // return new ArrayList<>();
            return new CopyOnWriteArrayList<>();
        }
        // List<String> movieIDs = new ArrayList<>();
        List<String> movieIDs = new CopyOnWriteArrayList<>();
        for(String movieID : userFavoriteMap.get(userID)){
            movieIDs.add(movieID);
        }
        List<Movie> movies = getMoviesByIDs(movieIDs);
        return movies;
    }

    @Override
    public void saveUserRating(String userID, String movieID, int ratingScore) {
        if(!userRatingMap.containsKey(userID)){
            // userRatingMap.put(userID, new HashMap<>());
            userRatingMap.put(userID, new ConcurrentHashMap<>());
        }
        userRatingMap.get(userID).put(movieID, ratingScore);
    }

    @Override
    public Map<String, Integer> getUserRatedMovies(String userID) {
        if(!userRatingMap.containsKey(userID)){
            // return new HashMap<>();
            return new ConcurrentHashMap<>();
        }
        return userRatingMap.get(userID);
    }

}
