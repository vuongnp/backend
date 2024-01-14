package com.vuongnp.film.concurrency.repository;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.vuongnp.film.concurrency.model.Movie;

class InMemoryRepositoryTest {
    private static InMemoryRepository inMemoryRepository;
    private static final Map<String, Movie> movieMap = new ConcurrentHashMap<>();
    private static final Map<String, Set<String>> userFavoriteMap = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, Integer>> userRatingMap = new ConcurrentHashMap<>();

    @BeforeAll
    public static void BeforeAll(){
        System.out.println("Start testing for InMemoryRepository");
        inMemoryRepository = new InMemoryRepository();
    }

    @AfterAll
    public static void AfterAll(){
        System.out.println("Finish testing for InMemoryRepository");
        inMemoryRepository = null;
    }

    @Test
    void testAddMovie_MovieInstance_NewMovieInMap(){
        // Arrange
        Movie movie = new Movie("0", "conan", 0);
        // Act
        inMemoryRepository.addMovie(movie);
        // Assert
        
    }
}
