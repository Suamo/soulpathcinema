package server.dao.impl;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import server.dao.ImdbMovieDao;
import server.utils.ImdbLinkBuilder;
import shared.MovieJson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by John Silver on 05.14.2015 14:43
 * See: http://www.omdbapi.com/
 */
public class ImdbMovieDaoImpl implements ImdbMovieDao {
    private static final Logger logger = Logger.getLogger(ImdbMovieDaoImpl.class);

    public MovieJson getMovieById(String imdbId) throws IOException {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpGet httpget = new HttpGet(new ImdbLinkBuilder().setId(imdbId).build());

        try {
            logger.info("Getting movieJson by request: " + httpget.getRequestLine());
            CloseableHttpResponse responseBody = httpClient.execute(httpget);

            ObjectMapper mapper = new ObjectMapper();
            MovieJson movieJson = mapper.readValue(responseBody.getEntity().getContent(), MovieJson.class);
            movieJson.setPoster("/img/movies/" + movieJson.getId() + ".jpg");
            return movieJson;
        } finally {
            httpClient.close();
        }
    }

    public MovieJson getMoviesByName(String name) throws IOException {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpGet httpget = new HttpGet(new ImdbLinkBuilder().setName(name).build());

        try {
            logger.info("Getting movieJson by request: " + httpget.getRequestLine());
            CloseableHttpResponse responseBody = httpClient.execute(httpget);

            ObjectMapper mapper = new ObjectMapper();
            MovieJson movieJson = mapper.readValue(responseBody.getEntity().getContent(), MovieJson.class);
            movieJson.setPoster("/img/movies/" + movieJson.getId() + ".jpg");
            return movieJson;
        } finally {
            httpClient.close();
        }
    }

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        System.out.println("saving to file..");
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;
        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }

    private MovieJson newUnknownMovie() {
        MovieJson movieJson = new MovieJson();
        movieJson.setTitle("Unknown");
        movieJson.setPoster("/img/unknown_token.svg");
        return movieJson;
    }
}
