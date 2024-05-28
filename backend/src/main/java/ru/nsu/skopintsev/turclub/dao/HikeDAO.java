package ru.nsu.skopintsev.turclub.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.nsu.skopintsev.turclub.mapper.HikeRowMapper;
import ru.nsu.skopintsev.turclub.models.Hike;

import java.util.List;

@Repository
public class HikeDAO implements DAO<Hike, Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HikeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Hike> findAll() {
        String sql = "SELECT h.id AS hike_id, h.name AS hike_name, h.plan_start_date, h.real_start_date, h.real_end_date, h.is_planned, " +
                "ht.id AS hike_type_id, ht.name AS hike_type_name, " +
                "t.id AS tourist_id, t.full_name, tt.id AS type_id, tt.name AS type_name, " +
                "r.id AS route_id, r.name AS route_name, r.length_meters " +
                "FROM hike h " +
                "JOIN hike_type ht ON h.hike_type_id = ht.id " +
                "JOIN tourist t ON h.instructor_id = t.id " +
                "JOIN tourist_type tt ON t.type_id = tt.id " +
                "JOIN route r ON h.route_id = r.id";
        return jdbcTemplate.query(sql, new HikeRowMapper());
    }

    @Override
    public Hike findById(Integer id) {
        String sql = "SELECT h.id AS hike_id, h.name AS hike_name, h.plan_start_date, h.real_start_date, h.real_end_date, h.is_planned, " +
                "ht.id AS hike_type_id, ht.name AS hike_type_name, " +
                "t.id AS tourist_id, t.full_name, tt.id AS type_id, tt.name AS type_name, " +
                "r.id AS route_id, r.name AS route_name, r.length_meters " +
                "FROM hike h " +
                "JOIN hike_type ht ON h.hike_type_id = ht.id " +
                "JOIN tourist t ON h.instructor_id = t.id " +
                "JOIN tourist_type tt ON t.type_id = tt.id " +
                "JOIN route r ON h.route_id = r.id " +
                "WHERE h.id = ?";
        return jdbcTemplate.queryForObject(sql, new HikeRowMapper(), id);
    }

    @Override
    public int save(Hike hike) {
        return jdbcTemplate.update("INSERT INTO hike (name, plan_start_date, real_start_date, real_end_date, is_planned, hike_type_id, instructor_id, route_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", hike.getName(),
                hike.getPlanStartDate(),
                hike.getRealStartDate(),
                hike.getRealEndDate(),
                hike.getIsPlanned(),
                hike.getHikeType().getId(),
                hike.getTourist().getId(),
                hike.getRoute().getId());
    }

    @Override
    public int update(Hike hike) {
        validation(hike);
        return jdbcTemplate.update("UPDATE hike SET name = ?, plan_start_date = ?, real_start_date = ?, real_end_date = ?, is_planned = ?, hike_type_id = ?, instructor_id = ?, route_id = ? WHERE id = ?",
                hike.getName(),
                hike.getPlanStartDate(),
                hike.getRealStartDate(),
                hike.getRealEndDate(),
                hike.getIsPlanned(),
                hike.getHikeType().getId(),
                hike.getTourist().getId(),
                hike.getRoute().getId(),
                hike.getId());
    }

    @Override
    public int deleteById(Integer id) {
        return jdbcTemplate.update("DELETE FROM hike WHERE id = ?", id);
    }

    public List<Hike.HikeType> findAllHikeTypes() {
        return jdbcTemplate.query("SELECT h.id, h.name  FROM hike_type h", new BeanPropertyRowMapper<>(Hike.HikeType.class));
    }

    private void validation(Hike hike) {
        if (hike.getName() == null || hike.getName().isEmpty()) {
            hike.setName(null);
        }
        if (hike.getPlanStartDate() == null) {
            hike.setPlanStartDate(null);
        }
        if (hike.getRealStartDate() == null) {
            hike.setRealStartDate(null);
        }
        if (hike.getRealEndDate() == null) {
            hike.setRealEndDate(null);
        }
    }
}
