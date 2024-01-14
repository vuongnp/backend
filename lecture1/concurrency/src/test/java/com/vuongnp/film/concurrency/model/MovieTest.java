package com.vuongnp.film.concurrency.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MovieTest {
    @Test
    void testGetId_MovieInstance_ReturnCorrectId(){
        Movie movie = new Movie("0", "abc", 0);
        String id = movie.getId();
        assertEquals("0", id);
    }
    @Test
    void testGetGenre_MovieInstance_ReturnCorrectRenre(){
        Movie movie = new Movie("0", "abc", 0);
        int genre = movie.getGenre();
        assertEquals(0, genre);
    }
    @Test
    void testToString_MovieInstanceMovieGenre_ReturnMovieString(){
        Movie movie = new Movie("0", "abc", 0);
        String movieStr = movie.toString();
        assertEquals("Movie [id=0, name=abc, genre=Movie]", movieStr);
    }
    @Test
    void testToString_MovieInstanceSeriesGenre_ReturnMovieString(){
        Movie movie = new Movie("0", "abc", 1);
        String movieStr = movie.toString();
        assertEquals("Movie [id=0, name=abc, genre=Series]", movieStr);
    }
}
