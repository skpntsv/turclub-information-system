package ru.nsu.skopintsev.turclub.models;

import lombok.Data;

@Data
public class Contacts {
    private Integer id;
    private String email;
    private String mainPhone;
    private String reservePhone;
    private String emergencyPhone;
}
