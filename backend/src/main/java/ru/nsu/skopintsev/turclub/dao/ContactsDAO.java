package ru.nsu.skopintsev.turclub.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.nsu.skopintsev.turclub.models.Contacts;
import ru.nsu.skopintsev.turclub.models.Section;

import java.util.List;

@Repository
public class ContactsDAO implements DAO<Contacts, Long> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ContactsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Contacts> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM contacts",
                new BeanPropertyRowMapper<>(Contacts.class));
    }

    @Override
    public Contacts findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM contacts WHERE id = ?",
                new BeanPropertyRowMapper<>(Contacts.class),
                id);
    }

    @Override
    public int save(Contacts contacts) {
        return jdbcTemplate.update(
                "INSERT INTO contacts (email, main_phone, reserve_phone, emergency_phone) VALUES (?, ?, ?, ?)",
                contacts.getEmail(),
                contacts.getMain_phone(),
                contacts.getReserve_phone(),
                contacts.getEmergency_phone()
        );
    }

    @Override
    public int update(Contacts contacts) {
        return jdbcTemplate.update(
                "UPDATE contacts SET email = ?, main_phone = ?, reserve_phone = ?, emergency_phone = ? WHERE id = ?",
                contacts.getEmail(),
                contacts.getMain_phone(),
                contacts.getReserve_phone(),
                contacts.getEmergency_phone(),
                contacts.getId()
        );
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update(
                "DELETE FROM contacts WHERE id = ?",
                id);
    }
}
