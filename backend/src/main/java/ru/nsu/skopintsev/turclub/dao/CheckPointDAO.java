package ru.nsu.skopintsev.turclub.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.nsu.skopintsev.turclub.models.CheckPoint;

import java.util.List;

@Repository
public class CheckPointDAO implements DAO<CheckPoint, Integer> {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public CheckPointDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CheckPoint> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM checkpoint",
                new BeanPropertyRowMapper<>(CheckPoint.class)
        );
    }

    @Override
    public CheckPoint findById(Integer id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM checkpoint WHERE id = ?",
                new BeanPropertyRowMapper<>(CheckPoint.class),
                id
        );
    }

    @Override
    public int save(CheckPoint checkPoint) {
        String sql = "INSERT INTO checkpoint (description, type, latitude, longitude) VALUES (?, ?, ?, ?) RETURNING id";
        Integer generatedId = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                checkPoint.getDescription(),
                checkPoint.getType(),
                checkPoint.getLatitude(),
                checkPoint.getLongitude()
        );
        if (generatedId != null) {
            return generatedId;
        } else {
            throw new IllegalStateException("Failed to retrieve generated id after insert");
        }
    }

    @Override
    public int update(CheckPoint checkPoint) {
        return jdbcTemplate.update(
                "UPDATE checkpoint SET description = ?, type = ?, latitude = ?, longitude = ? WHERE id = ?",
                checkPoint.getDescription(),
                checkPoint.getType(),
                checkPoint.getLatitude(),
                checkPoint.getLongitude(),
                checkPoint.getId()
        );
    }

    @Override
    public int deleteById(Integer id) {
        return jdbcTemplate.update(
                "DELETE FROM checkpoint WHERE id = ?",
                id
        );
    }
}
