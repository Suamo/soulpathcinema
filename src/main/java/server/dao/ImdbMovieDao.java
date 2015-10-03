package server.dao;

import shared.MovieJson;

import java.io.IOException;

/**
 * Created by John Silver on 06.23.2015 23:04
 */
public interface ImdbMovieDao {
    MovieJson getMovieById(String imdbId) throws IOException;
    MovieJson getMoviesByName(String name) throws IOException;
}
