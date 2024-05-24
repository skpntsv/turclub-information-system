package ru.nsu.skopintsev.turclub.models;

import lombok.Data;
import org.postgresql.util.PGInterval;

@Data
public class Route {
    private Integer id;
    private String name;
    private Integer lengthMeters;
    private PGInterval duration;
    private Integer difficultyCategory;
    private String description;
}
