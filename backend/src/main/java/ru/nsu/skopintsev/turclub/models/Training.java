package ru.nsu.skopintsev.turclub.models;

import lombok.Data;
import org.postgresql.util.PGInterval;

import java.sql.Date;

@Data
public class Training {
    private Integer id;
    private Date planeDate;
    private Date realDate;
    private String place;
    private PGInterval duration;

    private Integer trainerId;
    private Integer groupId;
    private Integer sectionId;
}
