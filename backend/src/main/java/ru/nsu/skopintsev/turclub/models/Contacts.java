package ru.nsu.skopintsev.turclub.models;

import lombok.Data;

@Data
public class Contacts {
    private Long id;
    private String email;
    private String main_phone;
    private String reserve_phone;
    private String emergency_phone;
}
