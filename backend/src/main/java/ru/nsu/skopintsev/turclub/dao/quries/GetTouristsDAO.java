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
public class GetTouristsDAO {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String pathToGetCountTouristByCriteria = "DML/queries/1/get_count_tourist_by_criteria.sql";
    private static final String pathToGetTouristByCriteria = "DML/queries/1/get_tourist_by_criteria.sql";

    @Autowired
    public GetTouristsDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public int getCountTouristByCriteria(Integer sectionId, Integer groupId, String gender,
                                         Integer birthdayStartYear, Integer birthdayEndYear,
                                         Integer minAge, Integer maxAge) throws DataAccessException {

        try {
            return Objects.requireNonNull(namedParameterJdbcTemplate.queryForObject(
                    loadQueryFromFile(pathToGetCountTouristByCriteria),
                    getMapSqlParameters(sectionId, groupId, gender, birthdayStartYear, birthdayEndYear, minAge, maxAge),
                    Integer.class));
        } catch (NullPointerException e) {
            log.error("Error execute query[get_count_tourist_by_criteria]", e);
            throw e;
        }
    }

    public List<Map<String, Object>> getTouristByCriteria(Integer sectionId, Integer groupId, String gender,
                                                          Integer birthdayStartYear, Integer birthdayEndYear,
                                                          Integer minAge, Integer maxAge) {

        return namedParameterJdbcTemplate.queryForList(
                loadQueryFromFile(pathToGetTouristByCriteria),
                getMapSqlParameters(sectionId, groupId, gender, birthdayStartYear, birthdayEndYear, minAge, maxAge));
    }

    private MapSqlParameterSource getMapSqlParameters(Integer sectionId, Integer groupId, String gender,
                                                      Integer birthdayStartYear, Integer birthdayEndYear,
                                                      Integer minAge, Integer maxAge) {
        if (gender.trim().isEmpty()) {
            gender = null;
        }

        return new MapSqlParameterSource()
                .addValue("section_id", sectionId, Types.INTEGER)
                .addValue("group_id", groupId, Types.INTEGER)
                .addValue("gender", gender, Types.VARCHAR)
                .addValue("birthday_start_year", birthdayStartYear, Types.INTEGER)
                .addValue("birthday_end_year", birthdayEndYear, Types.INTEGER)
                .addValue("min_age", minAge, Types.INTEGER)
                .addValue("max_age", maxAge, Types.INTEGER);
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
