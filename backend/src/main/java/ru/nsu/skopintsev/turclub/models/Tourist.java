package ru.nsu.skopintsev.turclub.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Data
public class Tourist {
    private Long id;
    private String fullName;
    private String gender;
    private Date birthday;
    private int category;
    private Long typeId;
    private Long contactId;
}
