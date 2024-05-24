package ru.nsu.skopintsev.turclub.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.nsu.skopintsev.turclub.models.SuperVisor;

import java.util.List;

@Repository
public class SuperVisorDAO implements DAO<SuperVisor, Integer> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SuperVisorDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<SuperVisor> findAll() {
        String sql = "SELECT * FROM SuperVisor";
        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(SuperVisor.class));
    }

    @Override
    public SuperVisor findById(Integer id) {
        String sql = "SELECT * FROM SuperVisor WHERE id = ?";
        return jdbcTemplate.queryForObject(
                sql,
                new BeanPropertyRowMapper<>(SuperVisor.class),
                id);
    }

    @Override
    public int save(SuperVisor supervisor) {
        String sql = "INSERT INTO SuperVisor (full_name, salary, hire_date, birthday, contact_id) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                supervisor.getFullName(),
                supervisor.getSalary(),
                new java.sql.Date(supervisor.getHireDate().getTime()),
                new java.sql.Date(supervisor.getBirthday().getTime()),
                supervisor.getContactId()
        );
    }

    @Override
    public int update(SuperVisor supervisor) {
        String sql = "UPDATE SuperVisor SET full_name = ?, salary = ?, hire_date = ?, birthday = ?, contact_id = ? WHERE id = ?";
        return jdbcTemplate.update(
                sql,
                supervisor.getFullName(),
                supervisor.getSalary(),
                supervisor.getHireDate(),
                supervisor.getBirthday(),
                supervisor.getContactId(),
                supervisor.getId()
        );
    }

    @Override
    public int deleteById(Integer id) {
        String sql = "DELETE FROM SuperVisor WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
