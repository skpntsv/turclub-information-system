package ru.nsu.skopintsev.turclub.models;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Hike {
    private Integer id;
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date planStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date realStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
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
