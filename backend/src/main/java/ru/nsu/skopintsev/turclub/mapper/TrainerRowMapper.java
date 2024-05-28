package ru.nsu.skopintsev.turclub.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.nsu.skopintsev.turclub.models.Section;
import ru.nsu.skopintsev.turclub.models.Trainer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrainerRowMapper implements RowMapper<Trainer> {
    @Override
    public Trainer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Trainer trainer = new Trainer();
        trainer.setId(rs.getInt("id"));
        trainer.setSalary(rs.getDouble("salary"));
        trainer.setHireDate(rs.getDate("hire_date"));
        trainer.setTerminationDate(rs.getDate("termination_date"));

        Trainer.Specialization specialization = new Trainer.Specialization();
        specialization.setId(rs.getInt("specialization_id"));
        specialization.setName(rs.getString("specialization_name"));


        Section section = new Section();
        section.setId(rs.getInt("section_id"));
        section.setName(rs.getString("section_name"));

        trainer.setSpecialization(specialization);
        trainer.setSection(section);

        return trainer;
    }
}