package ru.nsu.skopintsev.turclub.dao.quries;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Repository
public class GetTrainerDAO {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String pathToGetTrainerByCriteria = "DML/queries/2/get_trainer_by_criteria.sql";
    private static final String pathToGetCountTrainerByCriteria = "DML/queries/2/get_count_trainer_by_criteria.sql";

    @Autowired
    public GetTrainerDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public int getCountTrainerByCriteria(Integer sectionId, String gender,
                                         Integer age, Integer minSalary,
                                         Integer maxSalary, Integer specializationId) {

        try {
            return Objects.requireNonNull(namedParameterJdbcTemplate.queryForObject(
                    loadQueryFromFile(pathToGetCountTrainerByCriteria),
                    getMapSqlParameters(sectionId, gender, age, minSalary, maxSalary, specializationId),
                    Integer.class));
        } catch (NullPointerException e) {
            log.error("Error execute query[get_count_tourist_by_criteria]", e);
            throw e;
        }
    }

    public List<Map<String, Object>> getTrainerByCriteria(Integer sectionId, String gender,
                                                          Integer age, Integer minSalary,
                                                          Integer maxSalary, Integer specializationId) {

        return namedParameterJdbcTemplate.queryForList(
                loadQueryFromFile(pathToGetTrainerByCriteria),
                getMapSqlParameters(sectionId, gender, age, minSalary, maxSalary, specializationId));
    }

    private MapSqlParameterSource getMapSqlParameters(Integer sectionId, String gender,
                                                      Integer age, Integer minSalary,
                                                      Integer maxSalary, Integer specializationId) {
        if (gender.trim().isEmpty()) {
            gender = null;
        }

        return new MapSqlParameterSource()
                .addValue("section_id", sectionId, Types.INTEGER)
                .addValue("gender", gender, Types.VARCHAR)
                .addValue("age", age, Types.INTEGER)
                .addValue("min_salary", minSalary, Types.INTEGER)
                .addValue("max_salary", maxSalary, Types.INTEGER)
                .addValue("specialization_id", specializationId, Types.INTEGER);

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
