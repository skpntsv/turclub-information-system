package ru.nsu.skopintsev.turclub.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Data
@Getter
@Setter
public class Tourist {
    private Long id;
    private String fullName;
    private String gender;
    private Date birthday;
    private int category;
    private int typeId;
    private int contactId;

    public Tourist(String fullName, String gender, Date birthday, int category, int typeId, int contactId) {
        this.fullName = fullName;
        this.gender = gender;
        this.birthday = birthday;
        this.category = category;
        this.typeId = typeId;
        this.contactId = contactId;
    }

    public Tourist() {

    }
}
