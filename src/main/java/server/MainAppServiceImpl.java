package server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import client.rpc.MainAppService;
import server.dao.ImdbMovieDao;
import server.dao.impl.ImdbMovieDaoImpl;
import server.utils.TokensUtils;
import shared.MapDto;
import shared.Movie;
import shared.TokenDto;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by John Silver on 05.14.2015 14:33
 */
public class MainAppServiceImpl extends RemoteServiceServlet implements MainAppService {
    private static final Logger logger = Logger.getLogger(MainAppServiceImpl.class);

    ImdbMovieDao imdbMovieDao = new ImdbMovieDaoImpl();
    private MapDto mapDto;

    public MapDto getMap() {
        BasicConfigurator.configure();
//        if (mapDto != null) {
//            return mapDto;
//        }
        try {
            mapDto = new MapDto();
            InputStream stream = MainAppServiceImpl.class.getResourceAsStream("/map.svg");
            String map = IOUtils.toString(stream, "utf-8");
            mapDto.setMap(map);
            TokensUtils.generateTokens();
            for (TokenDto dto : TokensUtils.generateTokens()) {
                Movie movie = imdbMovieDao.getMovie(dto.getImdbId());
                dto.setMovie(movie);
                mapDto.addMovieToken(dto.getDomId(), dto);
            }
            return mapDto;
        } catch (IOException e) {
            logger.error(e, e);
            throw new RuntimeException("Unable to obtain Map model. ", e);
        }
    }
}