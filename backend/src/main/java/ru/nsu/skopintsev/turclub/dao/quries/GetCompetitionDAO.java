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
public class GetCompetitionDAO {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String pathToGetCompetitionBySection = "DML/queries/3/get_competition_by_section.sql";
    private static final String pathToGetCountCompetitionBySection = "DML/queries/3/get_count_competition_by_section.sql";

    @Autowired
    public GetCompetitionDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public int getCountCompetitionBySection(Integer sectionId) throws DataAccessException {
        try {
            return Objects.requireNonNull(namedParameterJdbcTemplate.queryForObject(
                    loadQueryFromFile(pathToGetCountCompetitionBySection),
                    getMapSqlParameters(sectionId),
                    Integer.class));
        } catch (NullPointerException e) {
            log.error("Error execute query[get_count_tourist_by_criteria]", e);
            throw e;
        }
    }

    public List<Map<String, Object>> getCompetitionBySection(Integer sectionId) {

        return namedParameterJdbcTemplate.queryForList(
                loadQueryFromFile(pathToGetCompetitionBySection),
                getMapSqlParameters(sectionId));
    }

    private MapSqlParameterSource getMapSqlParameters(Integer sectionId) {
        return new MapSqlParameterSource()
                .addValue("section_id", sectionId, Types.INTEGER);
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
