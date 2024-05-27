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
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class GetRouteDAO {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String pathToGetRouteByCriteria = "DML/queries/8/get_route_by_criteria.sql";
    @Autowired
    public GetRouteDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Map<String, Object>> getRouteByCriteria(Integer sectionId, Integer instructorId, Integer groupCount, Timestamp startDate, Timestamp endDate) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("section_id", sectionId, Types.INTEGER);
        parameterSource.addValue("instructor_id", instructorId, Types.INTEGER);
        parameterSource.addValue("start_date", startDate, Types.TIMESTAMP);
        parameterSource.addValue("end_date", endDate, Types.TIMESTAMP);
        parameterSource.addValue("group_count", groupCount, Types.INTEGER);

        return namedParameterJdbcTemplate.queryForList(
                loadQueryFromFile(pathToGetRouteByCriteria),
                parameterSource);
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
