package ru.nsu.skopintsev.turclub.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.nsu.skopintsev.turclub.models.Route;

import java.util.List;

@Repository
public class RouteDAO implements DAO<Route, Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RouteDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Route> findAll() {

        return jdbcTemplate.query(
                "SELECT * FROM route",
                new BeanPropertyRowMapper<>(Route.class));
    }

    @Override
    public Route findById(Integer id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM route WHERE id = ?",
                new BeanPropertyRowMapper<>(Route.class),
                id);
    }

    @Override
    public int save(Route route) {
        return jdbcTemplate.update(
                "INSERT INTO route (name, length_meters, duration, difficulty_category, description) VALUES (?, ?, ?, ?, ?)",
                route.getName(),
                route.getLengthMeters(),
                route.getDuration(),
                route.getDifficultyCategory(),
                route.getDescription());
    }

    @Override
    public int update(Route route) {
        return jdbcTemplate.update(
                "UPDATE route SET name = ?, length_meters = ?, duration = ?, difficulty_category = ?, description = ? WHERE id = ?",
                route.getName(),
                route.getLengthMeters(),
                route.getDuration(),
                route.getDifficultyCategory(),
                route.getDescription(),
                route.getId());
    }

    @Override
    public int deleteById(Integer id) {
        return jdbcTemplate.update(
                "DELETE FROM route WHERE id = ?",
                id);
    }
}
