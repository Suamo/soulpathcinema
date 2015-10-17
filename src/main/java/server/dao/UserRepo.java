package server.dao;

import org.springframework.data.repository.CrudRepository;
import shared.entity.User;

/**
 * Created by John Silver on 06.12.2015 12:36
 */
public interface UserRepo extends CrudRepository<User, Integer> {
    User findByEmailAndPassword(String email, String password);
}
