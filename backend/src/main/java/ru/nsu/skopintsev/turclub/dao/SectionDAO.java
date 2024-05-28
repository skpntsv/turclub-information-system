package ru.nsu.skopintsev.turclub.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.nsu.skopintsev.turclub.models.Section;

import java.util.List;

@Repository
public class SectionDAO implements DAO<Section, Long> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SectionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Section> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM section",
                new BeanPropertyRowMapper<>(Section.class));
    }

    @Override
    public Section findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM section WHERE id = ?",
                new BeanPropertyRowMapper<>(Section.class),
                id);
    }

    @Override
    public int save(Section section) {
        return jdbcTemplate.update(
                "INSERT INTO section (name, description, supervisor_id) VALUES (?, ?, ?)",
                section.getName(),
                section.getDescription(),
                section.getSupervisorId()
        );
    }

    @Override
    public int update(Section section) {
        return jdbcTemplate.update(
                "UPDATE section SET name = ?, description = ?, supervisor_id = ? WHERE id = ?",
                section.getName(),
                section.getDescription(),
                section.getSupervisorId(),
                section.getId()
        );
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update(
                "DELETE FROM section WHERE id = ?",
                id);
    }
}
