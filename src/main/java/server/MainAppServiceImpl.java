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
import server.dao.TokenRepo;
import server.entity.Movie;
import server.entity.Person;
import server.entity.Token;
import server.utils.TokensUtils;
import shared.MapDto;
import shared.MovieDto;
import shared.PersonDto;
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

    private TokenRepo repository;

    public MapDto getMap() {
        initApplication();

        MapDto mapDto = new MapDto();
        mapDto.setMap(obtainMap());
        for (TokenDto dto : TokensUtils.generateTokens()) {
            Token token = repository.findByDomId(dto.getDomId());
            if (token != null) {
                dto.setMovie(convertMovie(token.getMovie()));
                dto.setPerson(convertPerson(token.getPerson()));
            }
            mapDto.addToken(dto.getDomId(), dto);
        }
        return mapDto;
    }

    private PersonDto convertPerson(Person person) {
        if (person == null) {
            return null;
        }
        return new PersonDto(person.getId(), person.getName());
    }

    public void saveToken(TokenDto dto) {
        repository.save(convertTokenDto(dto));
    }

    private MovieDto convertMovie(Movie movie) {
        if (movie == null) {
            return null;
        }
        return new MovieDto(movie.getId(), movie.getName(), movie.getDirector());
    }

    private Token convertTokenDto(TokenDto tokendto) {
        Token token = new Token();
        token.setDomId(tokendto.getDomId());
        if(tokendto.getPerson() != null) {
            Person person = new Person();
            person.setId(tokendto.getPerson().getId());
            person.setName(tokendto.getPerson().getName());
            token.setPerson(person);
            token.setMovie(null);
        } else {
            Movie movie = new Movie();
            movie.setId(tokendto.getMovie().getId());
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
            repository = context.getBean(TokenRepo.class);
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