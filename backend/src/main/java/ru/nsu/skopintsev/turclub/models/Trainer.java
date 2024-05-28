package ru.nsu.skopintsev.turclub.models;

import lombok.Data;

import java.sql.Date;

@Data
public class Trainer {
    private Integer id;
    private Double salary;
    private Date hireDate;
    private Date terminationDate;
    private Specialization specialization;
    private Section section;

    @Data
    public static class Specialization {
        Integer id;
        String name;
    }

    public Trainer() {
        this.specialization = new Specialization();
        this.section = new Section();
    }
}
