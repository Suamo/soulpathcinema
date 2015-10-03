package shared.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by John Silver on 05.19.2015 19:57
 */
@Entity
@Table(name = "movie")
public class Movie implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String director;

    @Column(name = "imdb_id")
    private String imdbId;
    @Column(name = "imdb_rating")
    private String imdbRating;
    private String actors;
    private String awards;
    private String writer;

    public Movie() {
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

    public String getImdbId() {
        return imdbId;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getActors() {
        return actors;
    }

    public String getAwards() {
        return awards;
    }

    public String getWriter() {
        return writer;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
}
