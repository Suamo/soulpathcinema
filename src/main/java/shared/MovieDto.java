package shared;

import java.io.Serializable;

/**
 * Created by John Silver on 05.19.2015 19:57
 */
public class MovieDto implements Serializable{
    private Integer id;
    private String name;
    private String director;
    private String domId;

    public MovieDto() {
    }

    public MovieDto(Integer id, String domId, String name, String director) {
        this.id = id;
        this.domId = domId;
        this.name = name;
        this.director = director;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDomId() {
        return domId;
    }

    public void setDomId(String domId) {
        this.domId = domId;
    }
}
