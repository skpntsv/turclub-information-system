package ru.nsu.skopintsev.turclub.models;

import lombok.Data;

@Data
public class Section {
    private Integer id;
    private String name;
    private String description;
    private Integer supervisorId;
}
