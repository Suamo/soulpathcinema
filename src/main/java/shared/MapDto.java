package shared;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by John Silver on 08.23.2015 23:05
 */
public class MapDto implements Serializable {
    private String map;
    private HashMap<String, TokenDto> tokens = new HashMap<String, TokenDto>();

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public void addToken(String id, TokenDto token) {
        this.tokens.put(id, token);
    }

    public HashMap<String, TokenDto> getTokens() {
        return tokens;
    }
}
