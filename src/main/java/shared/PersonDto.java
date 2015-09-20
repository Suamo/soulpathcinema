package shared;

import java.io.Serializable;

/**
 * Created by John Silver on 20.21.2015 21:22
 */
public class PersonDto implements Serializable {
    private Integer id;
    private String domId;
    private String name;

    public PersonDto() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
