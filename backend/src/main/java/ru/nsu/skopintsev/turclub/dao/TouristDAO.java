package ru.nsu.skopintsev.turclub.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.nsu.skopintsev.turclub.mapper.TouristRowMapper;
import ru.nsu.skopintsev.turclub.models.Tourist;

import java.util.List;

@Slf4j
@Repository
public class TouristDAO implements DAO<Tourist, Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Value("${tourist.default.type}")
    private String defaultTouristType;

    @Autowired
    public TouristDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Tourist> findAll() {
        log.debug("Получение всех туристов");
        String sql = "SELECT t.id, t.full_name, t.gender, t.birthday, t.category, " +
                "tt.id as type_id, tt.name as type_name, " +
                "c.id as contact_id, c.email, c.main_phone, c.reserve_phone, c.emergency_phone " +
                "FROM tourist t " +
                "JOIN tourist_type tt ON t.type_id = tt.id " +
                "JOIN contacts c ON t.contact_id = c.id";
        return jdbcTemplate.query(sql, new TouristRowMapper());
    }

    @Override
    public Tourist findById(Integer id) {
        String sql = "SELECT t.id, t.full_name, t.gender, t.birthday, t.category, " +
                "tt.id as type_id, tt.name as type_name, " +
                "c.id as contact_id, c.email, c.main_phone, c.reserve_phone, c.emergency_phone " +
                "FROM tourist t " +
                "JOIN tourist_type tt ON t.type_id = tt.id " +
                "JOIN contacts c ON t.contact_id = c.id " +
                "WHERE t.id = ?";
        return jdbcTemplate.queryForObject(sql, new TouristRowMapper(), id);
    }

    @Override
    public int save(Tourist tourist) {
        String sql = "INSERT INTO tourist (full_name, gender, birthday, category, type_id, contact_id) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        Integer generatedId = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                tourist.getFullName(),
                tourist.getGender(),
                tourist.getBirthday(),
                tourist.getCategory(),
                tourist.getType().getId(),
                tourist.getContacts().getId()
        );

        if (generatedId == null) {
            throw new IllegalStateException("Failed to retrieve generated id after insert");
        }
        return generatedId;
    }

    @Override
    public int update(Tourist tourist) {
        return jdbcTemplate.update(
                "UPDATE tourist SET full_name = ?, gender = ?, birthday = ?, category = ?, type_id = ?, contact_id = ? WHERE id = ?",
                tourist.getFullName(),
                tourist.getGender(),
                tourist.getBirthday(),
                tourist.getCategory(),
                tourist.getType().getId(),
                tourist.getContacts().getId(),
                tourist.getId()
        );
    }

    @Override
    public int deleteById(Integer id) {
        return jdbcTemplate.update(
                "DELETE FROM tourist WHERE id = ?",
                id);
    }

    public Integer findDefaultTouristType() {
        return jdbcTemplate.queryForObject(
                "SELECT id FROM tourist_type WHERE UPPER(name) = UPPER(?)",
                Integer.class,
                defaultTouristType);
    }

    public List<Tourist.TouristType> findAllTouristType() {
        log.debug("Получение всех типов туристов");
        String sql = "SELECT * FROM tourist_type";
        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(Tourist.TouristType.class));
    }

    public Tourist.TouristType findTouristTypeById(Integer id) {
        String sql = "SELECT id FROM tourist_type WHERE id = ?";
        return jdbcTemplate.queryForObject(
                sql,
                new BeanPropertyRowMapper<>(Tourist.TouristType.class),
                id);
    }

    public List<Tourist> findAllInstructors() {
        String sql = "SELECT\n" +
                "    t.id,\n" +
                "    t.full_name,\n" +
                "    t.gender,\n" +
                "    t.birthday,\n" +
                "    t.category,\n" +
                "    tt.id AS type_id,\n" +
                "    tt.name AS type_name,\n" +
                "    c.id AS contact_id,\n" +
                "    c.email,\n" +
                "    c.main_phone,\n" +
                "    c.reserve_phone,\n" +
                "    c.emergency_phone\n" +
                "FROM\n" +
                "    Tourist t\n" +
                "JOIN\n" +
                "    Tourist_type tt ON t.type_id = tt.id\n" +
                "JOIN\n" +
                "    Contacts c ON t.contact_id = c.id\n" +
                "WHERE\n" +
                "    UPPER(tt.name) != UPPER(?);\n";
        return jdbcTemplate.query(sql, new TouristRowMapper(), defaultTouristType);
    }
}
