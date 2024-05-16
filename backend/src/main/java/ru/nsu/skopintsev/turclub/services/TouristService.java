package ru.nsu.skopintsev.turclub.services;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.skopintsev.turclub.dao.ContactsDAO;
import ru.nsu.skopintsev.turclub.dao.TouristDAO;
import ru.nsu.skopintsev.turclub.models.Contacts;
import ru.nsu.skopintsev.turclub.models.Tourist;

import java.util.List;

@Service
@Transactional
@Slf4j
public class TouristService {
    private final TouristDAO touristDAO;
    private final ContactsDAO contactsDAO;
    private final JdbcTemplate jdbcTemplate;

    private static int defaultTouristTypeId;

    @Value("${tourist.default.type}")
    private String defaultTouristType;

    @Value("${error.default_tourist_type_not_found}")
    private String defaultTouristTypeNotFoundMessage;

    @Autowired
    public TouristService(TouristDAO touristDAO, ContactsDAO contactsDAO, JdbcTemplate jdbcTemplate) {
        this.touristDAO = touristDAO;
        this.contactsDAO = contactsDAO;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void initDefaultTouristType() {
        try {
            Integer typeId = jdbcTemplate.queryForObject(
                    "SELECT id FROM tourist_type WHERE UPPER(name) = UPPER(?)",
                    Integer.class,
                    defaultTouristType
            );
            if (typeId != null) {
                defaultTouristTypeId = typeId;
                log.info("Default tourist type ID initialized: {}", defaultTouristTypeId);
            } else {
                throw new IllegalStateException(defaultTouristTypeNotFoundMessage);
            }
        } catch (Exception e) {
            log.error("Error initializing default tourist type ID", e);
            throw e;
        }
    }

    public List<Tourist> findAllTourists() {
        try {
            return touristDAO.findAll();
        } catch (Exception e) {
            log.error("Error fetching all tourists", e);
            throw e;
        }
    }

    public Tourist findTouristById(Integer id) {
        try {
            return touristDAO.findById(id);
        } catch (Exception e) {
            log.error("Error fetching tourist by ID: {}", id, e);
            throw e;
        }
    }

    public void saveTourist(Tourist tourist, Contacts contacts) {
        try {
            tourist.setContactId(contactsDAO.save(contacts));
            tourist.setTypeId(defaultTouristTypeId);
            touristDAO.save(tourist);
            log.info("Saved tourist: {}", tourist);
        } catch (Exception e) {
            log.error("Error saving tourist", e);
            throw e;
        }
    }

    public void updateTourist(Tourist tourist) {
        try {
            touristDAO.update(tourist);
            log.info("Updated tourist: {}", tourist);
        } catch (Exception e) {
            log.error("Error updating tourist", e);
            throw e;
        }
    }

    public void deleteTouristById(Integer id) {
        try {
            touristDAO.deleteById(id);
            log.info("Deleted tourist with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting tourist with ID: {}", id, e);
            throw e;
        }
    }
}
