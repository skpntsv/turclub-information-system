package ru.nsu.skopintsev.turclub.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.nsu.skopintsev.turclub.mapper.TrainerRowMapper;
import ru.nsu.skopintsev.turclub.models.Trainer;

import java.util.List;

@Slf4j
@Repository
public class TrainerDAO implements DAO<Trainer, Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TrainerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Trainer> findAll() {
        String sql = "SELECT t.id, t.salary, t.hire_date, t.section_id, t.specialization_id as specialization_id, " +
                "sp.name as specialization_name, t.termination_date, s.id as section_id, s.name as section_name " +
                "FROM trainer t " +
                "JOIN specialization sp ON t.specialization_id = sp.id " +
                "JOIN section s ON s.id = t.section_id ";
        return jdbcTemplate.query(sql, new TrainerRowMapper());
    }

    @Override
    public Trainer findById(Integer id) throws DataAccessException {
        String sql = "SELECT t.id, t.salary, t.hire_date, t.section_id, t.specialization_id as specialization_id, " +
                "sp.name as specialization_name, t.termination_date, s.id as section_id, s.name as section_name " +
                "FROM trainer t " +
                "JOIN specialization sp ON t.specialization_id = sp.id " +
                "JOIN section s ON s.id = t.section_id " +
                "WHERE t.id = ?";
        return jdbcTemplate.queryForObject(sql, new TrainerRowMapper(), id);
    }

    @Override
    public int save(Trainer trainer) {
        String sql = "INSERT INTO trainer (id, salary, hire_date, termination_date, specialization_id, section_id) VALUES (?, ?, ?, ?, ?) RETURNING id";
        Integer generatedId = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                trainer.getId(),
                trainer.getSalary(),
                trainer.getHireDate(),
                trainer.getTerminationDate(),
                trainer.getSpecialization().getId(),
                trainer.getSection().getId()
        );

        if (generatedId == null) {
            throw new IllegalStateException("Failed to retrieve generated id after insert");
        }
        return generatedId;
    }

    @Override
    public int update(Trainer trainer) {
        return jdbcTemplate.update(
                "UPDATE trainer SET id = ?, salary = ?, hire_date = ?, termination_date = ?, specialization_id = ?, section_id = ? WHERE id = ?",
                trainer.getId(),
                trainer.getSalary(),
                trainer.getHireDate(),
                trainer.getTerminationDate(),
                trainer.getSpecialization().getId(),
                trainer.getSection().getId(),
                trainer.getId()
        );
    }

    @Override
    public int deleteById(Integer id) {
        return jdbcTemplate.update(
                "DELETE FROM trainer WHERE id = ?",
                id);
    }

    public List<Trainer.Specialization> findAllSpecializations() {
        String sql = "SELECT * FROM specialization";
        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(Trainer.Specialization.class));
    }
}
