package server;

import client.rpc.MainAppService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.dao.ImdbMovieDao;
import server.dao.TokenRepo;
import shared.MapDto;
import shared.MovieJson;
import shared.entity.Movie;
import shared.entity.Token;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by John Silver on 05.14.2015 14:33
 */
@Service
@Transactional
public class MainAppServiceImpl extends RemoteServiceServlet implements MainAppService {
    private static final Logger logger = Logger.getLogger(MainAppServiceImpl.class);

    private ApplicationContext context;

    private ImdbMovieDao imdbMovieDao;
    private TokenRepo repository;

    public MapDto getMap() {
        initApplication();

        MapDto mapDto = new MapDto();
        mapDto.setMap(obtainMap());
        for (Token token : repository.findAll()) {
            mapDto.addToken(token.getDomId(), token);
        }
        return mapDto;
    }

    public Token saveToken(Token token) throws IOException {
        Movie movie = token.getMovie();
        if (movie != null)  {

            MovieJson imdbMovie;
            if (!StringUtils.isBlank(movie.getImdbId())) {
                imdbMovie = imdbMovieDao.getMovieById(movie.getImdbId());
            } else {
                imdbMovie = imdbMovieDao.getMoviesByName(movie.getName());
            }
            if (imdbMovie != null) {
                movie.setName(imdbMovie.getTitle());
                movie.setDirector(imdbMovie.getDirector());
                movie.setImdbId(imdbMovie.getId());
                movie.setImdbRating(imdbMovie.getImdbRating());
                movie.setActors(imdbMovie.getActors());
                movie.setAwards(imdbMovie.getAwards());
                movie.setWriter(imdbMovie.getWriter());
                movie.setCountry(imdbMovie.getCountry());
            }
        }
        return repository.save(token);
    }

    private void initApplication() {
        BasicConfigurator.configure();
        if (context == null) {
            context = new ClassPathXmlApplicationContext("applicationContext.xml");
        }
        if (repository == null) {
            repository = context.getBean(TokenRepo.class);
        }
        if (imdbMovieDao == null) {
            imdbMovieDao = context.getBean(ImdbMovieDao.class);
        }
    }

    private String obtainMap() {
        try {
            InputStream stream = MainAppServiceImpl.class.getResourceAsStream("/map.svg");
            return IOUtils.toString(stream, "utf-8");
        } catch (IOException e) {
            logger.error(e, e);
            throw new RuntimeException("Unable to obtain Map model. ", e);
        }
    }
}