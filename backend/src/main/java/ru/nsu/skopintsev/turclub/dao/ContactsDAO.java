package ru.nsu.skopintsev.turclub.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.nsu.skopintsev.turclub.models.Contacts;

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
        validContacts(contacts);

        String sql = "INSERT INTO contacts (email, main_phone, reserve_phone, emergency_phone) VALUES (?, ?, ?, ?) RETURNING id";
        Integer generatedId = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                contacts.getEmail(),
                contacts.getMainPhone(),
                contacts.getReservePhone(),
                contacts.getEmergencyPhone()
        );

        if (generatedId != null) {
            return generatedId;
        } else {
            throw new IllegalStateException("Failed to retrieve generated id after insert");
        }
    }

    @Override
    public int update(Contacts contacts) {
        validContacts(contacts);
        return jdbcTemplate.update(
                "UPDATE contacts SET email = ?, main_phone = ?, reserve_phone = ?, emergency_phone = ? WHERE id = ?",
                contacts.getEmail(),
                contacts.getMainPhone(),
                contacts.getReservePhone(),
                contacts.getEmergencyPhone(),
                contacts.getId()
        );
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update(
                "DELETE FROM contacts WHERE id = ?",
                id);
    }

    public void validContacts(Contacts contacts) {
        if (contacts.getEmergencyPhone() != null && contacts.getEmergencyPhone().trim().isEmpty()) {
            contacts.setEmergencyPhone(null);
        }

        if (contacts.getReservePhone() != null && contacts.getReservePhone().trim().isEmpty()) {
            contacts.setReservePhone(null);
        }
    }
}
