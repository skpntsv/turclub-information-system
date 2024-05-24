package ru.nsu.skopintsev.turclub.models;

import lombok.Data;

import java.sql.Date;

@Data
public class SuperVisor {
    private Long id;
    private String fullName;
    private double salary;
    private Date hireDate;
    private Date birthday;
    private int contactId;
}
