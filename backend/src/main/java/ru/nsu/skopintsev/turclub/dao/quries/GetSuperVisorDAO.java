package ru.nsu.skopintsev.turclub.dao.quries;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class GetSuperVisorDAO {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String pathToGetSuperVisorByCriteria = "DML/queries/6/get_super_visor_by_criteria.sql";

    @Autowired
    public GetSuperVisorDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Map<String, Object>> getSuperVisorByCriteria(Double minSalary, Double maxSalary, Integer minBirthYear,
                                                             Integer maxBirthYear, Date minHireDate, Date maxHireDate) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("min_salary", minSalary, Types.DOUBLE);
        mapSqlParameterSource.addValue("max_salary", maxSalary, Types.DOUBLE);
        mapSqlParameterSource.addValue("min_birth_year", minBirthYear, Types.INTEGER);
        mapSqlParameterSource.addValue("max_birth_year", maxBirthYear, Types.INTEGER);
        mapSqlParameterSource.addValue("min_hire_date", minHireDate, Types.DATE);
        mapSqlParameterSource.addValue("max_hire_date", maxHireDate, Types.DATE);


        return namedParameterJdbcTemplate.queryForList(
                loadQueryFromFile(pathToGetSuperVisorByCriteria),
                mapSqlParameterSource);
    }

    private String loadQueryFromFile(String fileName) {
        try {
            return Files.readString(Paths.get(new ClassPathResource(fileName).getFile().getPath()));
        } catch (IOException e) {
            log.error("Failed to load query from file: {}", fileName, e);
            throw new RuntimeException(e);
        }
    }
}
