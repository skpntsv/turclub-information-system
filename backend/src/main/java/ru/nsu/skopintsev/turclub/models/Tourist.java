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
    private Integer typeId;
    private Integer contactId;
}
