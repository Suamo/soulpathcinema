package server.dao;

import org.springframework.data.repository.CrudRepository;
import shared.entity.Token;

import java.util.List;

/**
 * Created by John Silver on 06.12.2015 12:36
 */
public interface TokenRepo extends CrudRepository<Token, Integer> {
    Token findByDomId(String domId);

//    @Query("select t from Token t where t.movie.userId like %?1")
    List<Token> findAll();
}
