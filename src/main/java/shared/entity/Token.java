package shared.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by John Silver on 20.22.2015 22:17
 */
@Entity
@Table(name = "token")
public class Token implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;
    @Column(name = "dom_id")
    String domId;

    @OneToOne(cascade = CascadeType.ALL)
    Movie movie;
    @OneToOne(cascade = CascadeType.ALL)
    Person person;

    @Column(name = "position_x")
    private String x;
    @Column(name = "position_y")
    private String y;
    @Column
    private String size;
    @Column(name = "category_id")
    private String categoryId;
    @Column(name = "category_name")
    private String categoryName;

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

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getSize() {
        return size;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
