package server.dao;

import org.springframework.data.repository.CrudRepository;
import server.entity.Movie;

/**
 * Created by John Silver on 06.12.2015 12:36
 */
public interface MainRepo extends CrudRepository<Movie, Integer> {
    Movie findByDomId(String domId);
}
