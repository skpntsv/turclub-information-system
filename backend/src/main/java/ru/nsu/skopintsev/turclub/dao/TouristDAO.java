package ru.nsu.skopintsev.turclub.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.nsu.skopintsev.turclub.models.Tourist;

import java.util.List;

@Slf4j
@Repository
public class TouristDAO implements DAO<Tourist, Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TouristDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Tourist> findAll() {
        log.debug("Получение всех туристов");
        return jdbcTemplate.query(
                "SELECT * FROM tourist",
                new BeanPropertyRowMapper<>(Tourist.class));
    }

    @Override
    public Tourist findById(Integer id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM tourist WHERE id = ?",
                new BeanPropertyRowMapper<>(Tourist.class),
                id);
    }

    @Override
    public int save(Tourist tourist) {
        return jdbcTemplate.update(
                "INSERT INTO tourist (full_name, gender, birthday, category, type_id, contact_id) VALUES (?, ?, ?, ?, ?, ?)",
                tourist.getFullName(),
                tourist.getGender(),
                tourist.getBirthday(),
                tourist.getCategory(),
                tourist.getTypeId(),
                tourist.getContactId()
        );
    }

    @Override
    public int update(Tourist tourist) {
        return jdbcTemplate.update(
                "UPDATE tourist SET full_name = ?, gender = ?, birthday = ?, category = ?, type_id = ?, contact_id = ? WHERE id = ?",
                tourist.getFullName(),
                tourist.getGender(),
                tourist.getBirthday(),
                tourist.getCategory(),
                tourist.getTypeId(),
                tourist.getContactId(),
                tourist.getId()
        );
    }

    @Override
    public int deleteById(Integer id) {
        return jdbcTemplate.update(
                "DELETE FROM tourist WHERE id = ?",
                id);
    }
}
