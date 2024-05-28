package ru.nsu.skopintsev.turclub.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.nsu.skopintsev.turclub.models.Groups;

import java.util.List;

@Repository
public class GroupsDAO implements DAO<Groups, Integer> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GroupsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Groups> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM groups",
                new BeanPropertyRowMapper<>(Groups.class)
        );
    }

    @Override
    public Groups findById(Integer id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM groups WHERE id = ?",
                new BeanPropertyRowMapper<>(Groups.class),
                id
        );
    }

    @Override
    public int save(Groups groups) {
        String sql = "INSERT INTO groups (name, description, section_id, trainer_id) VALUES (?, ?, ?, ?) RETURNING id";
        Integer generatedId = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                groups.getName(),
                groups.getDescription(),
                groups.getSectionId(),
                groups.getTrainerId()
        );
        if (generatedId != null) {
            return generatedId;
        } else {
            throw new IllegalStateException("Failed to retrieve generated id after insert");
        }
    }

    @Override
    public int update(Groups groups) {
        return jdbcTemplate.update(
                "UPDATE groups SET name = ?, description = ?, section_id = ?, trainer_id = ? WHERE id = ?",
                groups.getName(),
                groups.getDescription(),
                groups.getSectionId(),
                groups.getTrainerId(),
                groups.getId()
        );
    }

    @Override
    public int deleteById(Integer id) {
        return jdbcTemplate.update(
                "DELETE FROM groups WHERE id = ?",
                id
        );
    }
}
