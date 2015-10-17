package shared;

import shared.entity.User;

import java.io.Serializable;

/**
 * Created by John Silver on 17.13.2015 13:42
 */
public class UserDto implements Serializable {

    private Integer id;

    public UserDto() {
    }

    public UserDto(User user) {
        id = user.getId();
    }

    public Integer getId() {
        return id;
    }
}
