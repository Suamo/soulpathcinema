package server.utils;

import shared.TokenDto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

/**
 * Created by John Silver on 20.22.2015 22:52
 * <p/>
 * knownMovies.put("tt0110912", newToken("token_0_")); // pulp fiction
 * knownMovies.put("tt0418279", newToken("token_1_")); // transformers
 * knownMovies.put("tt0944947", newToken("token_2_")); // game of thrones
 * knownMovies.put("tt0046183", newToken("token_3_")); // peter pan
 */
public class TokensUtils {
    public static ArrayList<TokenDto> generateTokens() {
        ArrayList<TokenDto> knownMovies = new ArrayList<TokenDto>();

        InputStream inputStream = null;
        try {
            Properties prop = new Properties();
            String tokensFile = "tokens.properties";

            inputStream = TokensUtils.class.getClassLoader().getResourceAsStream(tokensFile);
            loadProperties(inputStream, prop, tokensFile);

            for (Map.Entry<Object, Object> tokenPropertyEntry : prop.entrySet()) {
                String[] tokenDetails = ((String) tokenPropertyEntry.getValue()).split(";");
                knownMovies.add(newToken((String) tokenPropertyEntry.getKey(), tokenDetails));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(inputStream);
        }

        return knownMovies;
    }

    private static void close(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadProperties(InputStream inputStream, Properties prop, String tokensFile) throws IOException {
        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("Property file '" + tokensFile + "' not found in the classpath");
        }
    }

    private static TokenDto newToken(String imdbId, String[] tokenDetails) {
        String domId = tokenDetails[0];
        String x = tokenDetails[1];
        String y = tokenDetails[2];
        String size = tokenDetails[3];

        TokenDto token = new TokenDto();
        token.setDomId(domId);
        token.setImdbId(imdbId);
        token.setX(x);
        token.setY(y);
        token.setSize(size);
        if (tokenDetails.length > 4) {
            token.setCategoryName(tokenDetails[4]);
            token.setCategoryId(tokenDetails[5]);
        }
        return token;
    }

}
