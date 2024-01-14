package com.vuongnp.film.concurrency.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    void testGetId_MovieInstance_ReturnCorrectId(){
        User adam = new User("0", "adam");
        String id = adam.getId();
        assertEquals("0", id);
    }
}
