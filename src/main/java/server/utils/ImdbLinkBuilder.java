package server.utils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by John Silver on 06.22.2015 22:48
 * Sample link: "http://www.omdbapi.com/?t=Game%20of%20Thrones&Season=1&Episode=1"
 *
 * Alternative:
 * http://www.imdb.com/xml/find?json=1&nr=1&tt=on&q=malinovke
 * https://github.com/simonbs/alfred-movies-workflow/blob/master/source/imdb.py
 */
public class ImdbLinkBuilder {
    /**
     * Parameter name: "i"
     * A valid IMDb ID (e.g. tt1285016)
     */
    private String id;
    private String name;

    public ImdbLinkBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public ImdbLinkBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public URI build() {
        try {
            String query = "";
            if (name != null) {
                query += "&t=" + name;
            }
            if (id != null) {
                query += "&i=" + id;
            }
            return new URI("http", "www.omdbapi.com", null, query, null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Cannot build IMDB url");
    }
}
