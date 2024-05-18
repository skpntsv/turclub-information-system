package ru.nsu.skopintsev.turclub.models;

import lombok.Data;
import java.sql.Date;

@Data
public class Tourist {
    private Integer id;
    private String fullName;
    private String gender;
    private Date birthday;
    private int category;
    private TouristType type;
    private Contacts contacts;

    @Data
    public static class TouristType {
        private Integer id;
        private String name;
    }

    public Tourist() {
        this.type = new TouristType();
        this.contacts = new Contacts();
    }
}
