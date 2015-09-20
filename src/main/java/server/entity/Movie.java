package server.entity;

import javax.persistence.*;

/**
 * Created by John Silver on 05.19.2015 19:57
 */
@Entity
@Table(name = "movies")
public class Movie {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    Integer id;
    String name;
    String director;
    @Column(name = "dom_id")
    String domId;

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

    public String getDomId() {
        return domId;
    }

    public void setDomId(String domId) {
        this.domId = domId;
    }
}
