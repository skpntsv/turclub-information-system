package ru.nsu.skopintsev.turclub.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
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
        return jdbcTemplate.query("SELECT * FROM hike", new BeanPropertyRowMapper<>(Hike.class));
    }

    @Override
    public Hike findById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM hike WHERE id = ?", new BeanPropertyRowMapper<>(Hike.class), id);
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
