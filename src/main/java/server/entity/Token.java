package server.entity;

import javax.persistence.*;

/**
 * Created by John Silver on 20.22.2015 22:17
 */
@Entity
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;
    @Column(name = "dom_id")
    String domId;

    @OneToOne
    Movie movie;
    @OneToOne
    Person person;

    public Token() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDomId() {
        return domId;
    }

    public void setDomId(String domId) {
        this.domId = domId;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
