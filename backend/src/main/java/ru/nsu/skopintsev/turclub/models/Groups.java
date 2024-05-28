package ru.nsu.skopintsev.turclub.models;

import lombok.Data;

@Data
public class Groups {
    private Integer id;
    private String name;
    private String description;
    private Integer sectionId;
    private Integer trainerId;
}
