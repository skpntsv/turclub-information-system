package ru.nsu.skopintsev.turclub.models;

import lombok.Data;

@Data
public class CheckPoint {
    private Integer id;
    private String description;
    private String type;
    private Double latitude;
    private Double longitude;
}
