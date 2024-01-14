package com.vuongnp.film.concurrency.model;

public class Movie {
    private String id;
    private String name;
    private int genre;

    public Movie(String id, String name, int genre) {
        this.id = id;
        this.name = name;
        this.genre = genre;
    }
    // public String getName() {
    //     return name;
    // }
    // public void setName(String name) {
    //     this.name = name;
    // }
    public String getId() {
        return id;
    }
    // public void setId(String id) {
    //     this.id = id;
    // }
    public int getGenre() {
        return genre;
    }
    // public void setGenre(int genre) {
    //     this.genre = genre;
    // }
    @Override
    public String toString() {
        String genreStr = "Movie";
        if(genre == 1){
            genreStr = "Series";
        }
        return "Movie [id=" + id + ", name=" + name + ", genre=" + genreStr + "]";
    }
}
