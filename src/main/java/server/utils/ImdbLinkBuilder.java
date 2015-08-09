package server.utils;

/**
 * Created by John Silver on 06.22.2015 22:48
 * Sample link: "http://www.omdbapi.com/?t=Game%20of%20Thrones&Season=1&Episode=1"
 */
public class ImdbLinkBuilder {
    /**
     * Parameter name: "i"
     * A valid IMDb ID (e.g. tt1285016)
     */
    private String id;

    public ImdbLinkBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public String build() {
        String link = "http://www.omdbapi.com/?";
        if (id != null) {
            link += "i=" + id + "&";
        }
        return link;
    }
}
