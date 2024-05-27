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
    private static final String pathToGetTouristByHike = "DML/queries/5/get_tourist_by_hike.sql";
    private static final String pathToGetCountTouristByHike = "DML/queries/5/get_count_tourist_by_hike.sql";

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

    // 5 запрос
    public int getCountTouristByHike(Integer sectionId, Integer groupId, Integer hikeId, Integer routeId, Integer pointId, Integer maxCategory, Integer minHikes) throws DataAccessException {
        try {
            return Objects.requireNonNull(namedParameterJdbcTemplate.queryForObject(
                    loadQueryFromFile(pathToGetCountTouristByHike),
                    getMapSqlParameters(sectionId, groupId, hikeId, routeId, pointId, maxCategory, minHikes),
                    Integer.class));
        } catch (NullPointerException e) {
            log.error("Error execute query[get_count_tourist_by_hike]", e);
            throw e;
        }
    }

    // 5 запрос
    public List<Map<String, Object>> getTouristByHike(Integer sectionId, Integer groupId, Integer hikeId, Integer routeId, Integer pointId, Integer maxCategory, Integer minHikes) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("section_id", sectionId, Types.INTEGER);
        mapSqlParameterSource.addValue("group_id", groupId, Types.INTEGER);
        mapSqlParameterSource.addValue("hike_id", hikeId, Types.INTEGER);
        mapSqlParameterSource.addValue("route_id", routeId, Types.INTEGER);
        mapSqlParameterSource.addValue("point_id", pointId, Types.INTEGER);
        mapSqlParameterSource.addValue("max_category", maxCategory, Types.INTEGER);
        mapSqlParameterSource.addValue("min_hikes", minHikes, Types.INTEGER);

        return namedParameterJdbcTemplate.queryForList(
                loadQueryFromFile(pathToGetTouristByHike),
                getMapSqlParameters(sectionId, groupId, hikeId, routeId, pointId, maxCategory, minHikes));
    }

    private MapSqlParameterSource getMapSqlParameters(Integer sectionId, Integer groupId, Integer hikeId, Integer routeId, Integer pointId, Integer maxCategory, Integer minHikes) {
        return new MapSqlParameterSource()
                .addValue("section_id", sectionId, Types.INTEGER)
                .addValue("group_id", groupId, Types.INTEGER)
                .addValue("hike_id", hikeId, Types.INTEGER)
                .addValue("route_id", routeId, Types.INTEGER)
                .addValue("point_id", pointId, Types.INTEGER)
                .addValue("max_category", maxCategory, Types.INTEGER)
                .addValue("min_hikes", minHikes, Types.INTEGER);
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
