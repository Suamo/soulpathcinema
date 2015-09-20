package server;

import client.rpc.MainAppService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.dao.MainRepo;
import server.entity.Movie;
import server.entity.Person;
import server.entity.Token;
import server.utils.TokensUtils;
import shared.MapDto;
import shared.MovieDto;
import shared.TokenDto;

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

    private MainRepo repository;

    public MapDto getMap() {
        initApplication();

        MapDto mapDto = new MapDto();
        mapDto.setMap(obtainMap());
        for (TokenDto dto : TokensUtils.generateTokens()) {
            dto.setMovie(convertMovie(dto.getDomId(), repository.findByDomId(dto.getDomId())));
            mapDto.addMovieToken(dto.getDomId(), dto);
        }
        return mapDto;
    }

    public void saveToken(TokenDto dto) {
//        repository.save(convertMovieDto(dto));
    }

    private MovieDto convertMovie(String domId, Movie movie) {
        if (movie == null) {
            return new MovieDto(null, domId, "", "");
        }
        return new MovieDto(movie.getId(), domId, movie.getName(), movie.getDirector());
    }

    private Token convertTokenDto(TokenDto tokendto) {
        Token token = new Token();
        if(tokendto.getPerson() != null) {
            Person person = new Person();
            person.setId(tokendto.getPerson().getId());
            person.setDomId(tokendto.getPerson().getDomId());
            person.setName(tokendto.getPerson().getName());
            token.setPerson(person);
            token.setMovie(null);
        } else {
            Movie movie = new Movie();
            movie.setId(tokendto.getMovie().getId());
            movie.setDomId(tokendto.getMovie().getDomId());
            movie.setName(tokendto.getMovie().getName());
            movie.setDirector(tokendto.getMovie().getDirector());
            token.setMovie(movie);
            token.setPerson(null);
        }

        return token;
    }

    private void initApplication() {
        BasicConfigurator.configure();
        if (context == null) {
            context = new ClassPathXmlApplicationContext("applicationContext.xml");
        }
        if (repository == null) {
            repository = context.getBean(MainRepo.class);
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