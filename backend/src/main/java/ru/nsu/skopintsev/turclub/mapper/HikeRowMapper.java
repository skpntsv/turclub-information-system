package ru.nsu.skopintsev.turclub.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.nsu.skopintsev.turclub.models.Hike;
import ru.nsu.skopintsev.turclub.models.Route;
import ru.nsu.skopintsev.turclub.models.Tourist;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HikeRowMapper implements RowMapper<Hike> {
    @Override
    public Hike mapRow(ResultSet rs, int rowNum) throws SQLException {
        Hike hike = new Hike();

        hike.setId(rs.getInt("hike_id"));
        hike.setName(rs.getString("hike_name"));
        hike.setPlanStartDate(rs.getDate("plan_start_date"));
        hike.setRealStartDate(rs.getDate("real_start_date"));
        hike.setRealEndDate(rs.getDate("real_end_date"));
        hike.setIsPlanned(rs.getBoolean("is_planned"));

        Hike.HikeType hikeType = new Hike.HikeType();
        hikeType.setId(rs.getInt("hike_type_id"));
        hikeType.setName(rs.getString("hike_type_name"));
        hike.setHikeType(hikeType);

        Tourist tourist = new Tourist();
        tourist.setId(rs.getInt("tourist_id"));
        tourist.setFullName(rs.getString("full_name"));

        Tourist.TouristType type = new Tourist.TouristType();
        type.setId(rs.getInt("type_id"));
        type.setName(rs.getString("type_name"));
        tourist.setType(type);

        hike.setTourist(tourist);

        Route route = new Route();
        route.setId(rs.getInt("route_id"));
        route.setName(rs.getString("route_name"));
        route.setLengthMeters(rs.getInt("length_meters"));
        route.setDifficultyCategory(rs.getInt("difficulty_category"));
        hike.setRoute(route);

        return hike;
    }
}
