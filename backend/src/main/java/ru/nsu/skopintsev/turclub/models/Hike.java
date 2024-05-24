package ru.nsu.skopintsev.turclub.models;

import lombok.Data;

import java.sql.Date;

@Data
public class Hike {
    private Integer id;
    private String name;
    private Date planStartDate;
    private Date realStartDate;
    private Date realEndDate;
    private Boolean isPlanned;

    private HikeType hikeType;
    private Tourist tourist;
    private Route route;

    @Data
    public static class HikeType {
        private Integer id;
        private String name;
    }

    public Hike() {
        this.hikeType = new HikeType();
        this.tourist = new Tourist();
        this.route = new Route();
    }
}
