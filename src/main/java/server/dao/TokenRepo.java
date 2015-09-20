package server.dao;

import org.springframework.data.repository.CrudRepository;
import server.entity.Token;

/**
 * Created by John Silver on 06.12.2015 12:36
 */
public interface TokenRepo extends CrudRepository<Token, Integer> {
    Token findByDomId(String domId);
}
