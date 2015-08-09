package server.dao.impl;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import server.dao.ImdbMovieDao;
import server.utils.ImdbLinkBuilder;
import shared.Movie;

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
    public static final String IGNORE_ID_PREFIX = "_xz_";
    private Movie UNKNOWN_MOVIE = newMovie();

    public Movie getMovie(String imdbId) throws IOException {
        if (imdbId.startsWith(IGNORE_ID_PREFIX)) {
            return UNKNOWN_MOVIE;
        }
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpGet httpget = new HttpGet(new ImdbLinkBuilder().setId(imdbId).build());

        try {
            logger.info("Gerring movie by request: " + httpget.getRequestLine());
            CloseableHttpResponse responseBody = httpClient.execute(httpget);

            ObjectMapper mapper = new ObjectMapper();
            Movie movie = mapper.readValue(responseBody.getEntity().getContent(), Movie.class);
            movie.setPoster("/img/movies/" + movie.getId() + ".jpg");
            return movie;
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

    private Movie newMovie() {
        Movie movie = new Movie();
        movie.setTitle("Unknown");
        movie.setPoster("/img/unknown_token.svg");
        return movie;
    }
}
