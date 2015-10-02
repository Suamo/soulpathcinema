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
import shared.MapDto;
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

    public void saveToken(Token dto) {
        repository.save(dto);
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