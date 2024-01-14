package com.vuongnp.film.concurrency.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.vuongnp.film.concurrency.exception.InvalidDataException;
import com.vuongnp.film.concurrency.model.Movie;
import com.vuongnp.film.concurrency.model.User;
import com.vuongnp.film.concurrency.repository.InMemoryRepository;

public class BusinessLogicTest {
    private static BusinessLogic businessLogic;
    private static InMemoryRepository inMemoryRepository;

    @BeforeAll
    public static void BeforeAll(){
        System.out.println("Start testing BusinessLogic");
        inMemoryRepository = mock(InMemoryRepository.class);
        businessLogic = new BusinessLogic(inMemoryRepository);
    }

    @Test
    void testAddMovie_ValidMovie_NoException() throws Exception{
        Movie movie = new Movie("0", "abc", 0);
        doNothing().when(inMemoryRepository).addMovie(movie);

        businessLogic.addMovie(movie);
        verify(inMemoryRepository).addMovie(movie);
    }
    @Test
    void testAddMovie_InvalidMovie_ThrowInvalidDataException(){
        Movie movie = new Movie("0", "abc", 2);

        assertThrows(InvalidDataException.class, ()->businessLogic.addMovie(movie));
    }
    @Test
    void testAddMovie_ExistedMovie_NoException() throws Exception{
        Movie movie = new Movie("123", "abc", 0);
        when(inMemoryRepository.hasMovie(movie.getId())).thenReturn(true);
        
        businessLogic.addMovie(movie);
        verify(inMemoryRepository).hasMovie(movie.getId());
    }

    @Test
    void testAddToFavoriteMovies_ValidMovieId_NoException() throws Exception{
        User adam = new User("0", "adam");
        String movieId = "0";
        when(inMemoryRepository.hasMovie(movieId)).thenReturn(true);
        doNothing().when(inMemoryRepository).addFavoriteMovie(adam.getId(), movieId);

        businessLogic.addToFavoriteMovies(adam, movieId);
        verify(inMemoryRepository).addFavoriteMovie(adam.getId(), movieId);
    }
    @Test
    void testAddToFavoriteMovies_InvalidMovieId_ThrowInvalidDataException(){
        User adam = new User("0", "adam");
        String movieId = "0";
        when(inMemoryRepository.hasMovie(movieId)).thenReturn(false);

        assertThrows(InvalidDataException.class, ()->businessLogic.addToFavoriteMovies(adam, movieId));
    }

    @Test
    void testRemoveFromFavoriteMovies_ValidMovieId_NoException() throws Exception{
        User adam = new User("0", "adam");
        String movieId = "0";
        when(inMemoryRepository.hasMovie(movieId)).thenReturn(true);
        doNothing().when(inMemoryRepository).removeFavoriteMovie(adam.getId(), movieId);

        businessLogic.removeFromFavoriteMovies(adam, movieId);
        verify(inMemoryRepository).removeFavoriteMovie(adam.getId(), movieId);
    }
    @Test
    void testRemoveFromFavoriteMovies_InvalidMovieId_ThrowInvalidDataException(){
        User adam = new User("0", "adam");
        String movieId = "0";
        when(inMemoryRepository.hasMovie(movieId)).thenReturn(false);

        assertThrows(InvalidDataException.class, ()->businessLogic.removeFromFavoriteMovies(adam, movieId));
    }

    @Test
    void testRateMovie_InvalidRatingScore_ThrowInvalidDataException(){
        User adam = new User("0", "adam");
        int ratingScore = 0;
        String movieId = "0";

        assertThrows(InvalidDataException.class, ()->businessLogic.rateMovie(adam, movieId, ratingScore));
    }
    @Test
    void testRateMovie_InvalidMovieId_ThrowInvalidDataException(){
        User adam = new User("0", "adam");
        String movieId = "0";
        int ratingScore = 1;
        when(inMemoryRepository.hasMovie(movieId)).thenReturn(false);

        assertThrows(InvalidDataException.class, ()->businessLogic.rateMovie(adam, movieId, ratingScore));
    }
    @Test
    void testRateMovie_ValidMovieId_NoException() throws Exception{
        User adam = new User("0", "adam");
        String movieId = "0";
        int ratingScore = 1;
        when(inMemoryRepository.hasMovie(movieId)).thenReturn(true);
        doNothing().when(inMemoryRepository).saveUserRating(adam.getId(), movieId, ratingScore);

        businessLogic.rateMovie(adam, movieId, ratingScore);
        verify(inMemoryRepository).saveUserRating(adam.getId(), movieId, ratingScore);
    }

    @Test
    void testGetUserFavoriteMovies_UserInstance_ReturnListFavoriteMovies() throws Exception{
        User adam = new User("0", "adam");
        Movie movie = new Movie("0", "abc", 0);
        List<Movie> movies = new CopyOnWriteArrayList<>();
        movies.add(movie);
        when(inMemoryRepository.getUserFavoriteMovies(adam.getId())).thenReturn(movies);
        
        List<Movie> favoriteMovies = businessLogic.getUserFavoriteMovies(adam);
        assertEquals(movies, favoriteMovies);
        verify(inMemoryRepository).getUserFavoriteMovies(adam.getId());
    }

    @Test
    void testGetUserRatedMovies_UserInstance_ReturnRatedMovies() throws Exception{
        User adam = new User("0", "adam");
        Movie movie = new Movie("0", "abc", 0);
        Map<Movie, Integer> results = new ConcurrentHashMap<>();
        int ratingScore = 5;
        results.put(movie, ratingScore);

        Map<String, Integer> userRatedMoviesIDs = new ConcurrentHashMap<>();
        userRatedMoviesIDs.put(movie.getId(), ratingScore);
        when(inMemoryRepository.getUserRatedMovies(adam.getId())).thenReturn(userRatedMoviesIDs);

        List<String> ratedMovieIds = new CopyOnWriteArrayList<>();
        ratedMovieIds.add(movie.getId());
        List<Movie> ratedMovies = new CopyOnWriteArrayList<>();
        ratedMovies.add(movie);
        when(inMemoryRepository.getMoviesByIDs(ratedMovieIds)).thenReturn(ratedMovies);
        
        Map<Movie, Integer> userRatedMovies = businessLogic.getUserRatedMovies(adam);
        assertEquals(results, userRatedMovies);
        verify(inMemoryRepository).getUserRatedMovies(adam.getId());
        verify(inMemoryRepository).getMoviesByIDs(ratedMovieIds);
    }
}
