package ru.nsu.skopintsev.turclub.dao.quries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Map;

@Repository
public class GetTourists {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public GetTourists(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public int getCountTouristByCriteria(Integer sectionId, Integer groupId, String gender, Integer birthdayStartYear, Integer birthdayEndYear, Integer minAge, Integer maxAge) {
        String sql = "SELECT COUNT(t.id) AS \"Количество туристов\" FROM Tourist t " +
                "JOIN Tourist_Groups tg ON t.id = tg.tourist_id " +
                "JOIN Groups g ON tg.group_id = g.id " +
                "JOIN Section s ON g.section_id = s.id " +
                "WHERE (s.id = :section_id OR :section_id IS NULL) " +
                "AND (g.id = :group_id OR :group_id IS NULL) " +
                "AND (t.gender = :gender OR :gender IS NULL) " +
                "AND (EXTRACT(YEAR FROM t.birthday) BETWEEN COALESCE(:birthday_start_year, EXTRACT(YEAR FROM t.birthday)) " +
                "AND COALESCE(:birthday_end_year, EXTRACT(YEAR FROM t.birthday))) " +
                "AND (DATE_PART('year', age(t.birthday)) BETWEEN COALESCE(:min_age, DATE_PART('year', age(t.birthday))) " +
                "AND COALESCE(:max_age, DATE_PART('year', age(t.birthday))));";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("section_id", sectionId, Types.INTEGER);
        params.addValue("group_id", groupId, Types.INTEGER);
        params.addValue("gender", gender, Types.VARCHAR);
        params.addValue("birthday_start_year", birthdayStartYear, Types.INTEGER);
        params.addValue("birthday_end_year", birthdayEndYear, Types.INTEGER);
        params.addValue("min_age", minAge, Types.INTEGER);
        params.addValue("max_age", maxAge, Types.INTEGER);

        //return jdbcTemplate.queryForObject(sql, Integer.class, params);
        //namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<Integer>(Integer.class));
        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public List<Map<String, Object>> getTouristsByCriteria(Integer sectionId, Integer groupId, String gender, Integer birthdayStartYear, Integer birthdayEndYear, Integer minAge, Integer maxAge) {
        String sql = "SELECT t.id as ID, t.full_name AS ФИО, tt.name AS Тип_туриста, " +
                "CASE WHEN t.gender = 'female' THEN 'Женский' WHEN t.gender = 'male' THEN 'Мужской' ELSE t.gender END AS Пол, " +
                "t.birthday AS \"Дата рождения\", t.category AS Категория, c.email AS Email, c.main_phone AS Основной_телефон, " +
                "c.reserve_phone AS \"Резервный телефон\", c.emergency_phone AS \"Экстренный телефон\", g.name AS \"Название группы\", " +
                "s.name AS \"Название секции\" FROM Tourist t " +
                "JOIN Tourist_Groups tg ON t.id = tg.tourist_id " +
                "JOIN Groups g ON tg.group_id = g.id " +
                "JOIN Section s ON g.section_id = s.id " +
                "JOIN Tourist_type tt ON t.type_id = tt.id " +
                "JOIN Contacts c ON t.contact_id = c.id " +
                "WHERE (s.id = :section_id OR :section_id IS NULL) " +
                "AND (g.id = :group_id OR :group_id IS NULL) " +
                "AND (t.gender = :gender OR :gender IS NULL) " +
                "AND (EXTRACT(YEAR FROM t.birthday) BETWEEN COALESCE(:birthday_start_year, EXTRACT(YEAR FROM t.birthday)) " +
                "AND COALESCE(:birthday_end_year, EXTRACT(YEAR FROM t.birthday))) " +
                "AND (DATE_PART('year', age(t.birthday)) BETWEEN COALESCE(:min_age, DATE_PART('year', age(t.birthday))) " +
                "AND COALESCE(:max_age, DATE_PART('year', age(t.birthday)))) " +
                "ORDER BY t.full_name;";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("section_id", sectionId)
                .addValue("group_id", groupId)
                .addValue("gender", gender)
                .addValue("birthday_start_year", birthdayStartYear)
                .addValue("birthday_end_year", birthdayEndYear)
                .addValue("min_age", minAge)
                .addValue("max_age", maxAge);

        //return jdbcTemplate.queryForList(sql, params);
        return namedParameterJdbcTemplate.queryForList(sql, params);
    }
}
