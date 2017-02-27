package com.example.omernaim.librarymovie;

/**
 * Created by OmerNaim on 07/02/2017.
 */

public class myMovies {


    String name;
    String body;
    String Url;
    String IMDB;

    public myMovies(String Name, String IMDB, String url, String body) {
        this.name = Name;
        this.IMDB = IMDB;
        this.Url = url;
        this.body = body;
    }

    public myMovies(String name, String url, String iMDB) {
        this.name = name;
        this.Url = url;
        this.IMDB = iMDB;
    }

    @Override
    public String toString() {
        return name;
    }
}
