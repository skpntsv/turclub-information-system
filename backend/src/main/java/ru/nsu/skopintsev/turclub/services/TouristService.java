package ru.nsu.skopintsev.turclub.services;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.skopintsev.turclub.dao.ContactsDAO;
import ru.nsu.skopintsev.turclub.dao.SectionDAO;
import ru.nsu.skopintsev.turclub.dao.TouristDAO;
import ru.nsu.skopintsev.turclub.dao.TrainerDAO;
import ru.nsu.skopintsev.turclub.models.Contacts;
import ru.nsu.skopintsev.turclub.models.Section;
import ru.nsu.skopintsev.turclub.models.Tourist;
import ru.nsu.skopintsev.turclub.models.Trainer;

import java.util.List;

@Service
@Transactional
@Slf4j
public class TouristService {
    private final TouristDAO touristDAO;
    private final ContactsDAO contactsDAO;

    private static Integer defaultTouristTypeId;
    private final TrainerDAO trainerDAO;
    private final SectionDAO sectionDAO;

    @Value("${error.default_tourist_type_not_found}")
    private String defaultTouristTypeNotFoundMessage;

    @Autowired
    public TouristService(TouristDAO touristDAO, ContactsDAO contactsDAO, TrainerDAO trainerDAO, SectionDAO sectionDAO) {
        this.touristDAO = touristDAO;
        this.contactsDAO = contactsDAO;
        this.trainerDAO = trainerDAO;
        this.sectionDAO = sectionDAO;
    }

    @PostConstruct
    private void initDefaultTouristType() {
        try {
            defaultTouristTypeId = touristDAO.findDefaultTouristType();
            if (defaultTouristTypeId != null) {
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

    public Trainer findTrainerById(Integer id) {
        try {
            return trainerDAO.findById(id);
        } catch (Exception e) {
            log.error("Error fetching tourist by ID: {}", id, e);
            throw e;
        }
    }

    public Tourist.TouristType findTouristTypeById(Integer id) {
        try {
            return touristDAO.findTouristTypeById(id);
        } catch (Exception e) {
            log.error("Error fetching tourist by ID: {}", id, e);
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

    public List<Tourist.TouristType> findAllTouristTypes() {
        try {
            return touristDAO.findAllTouristType();
        } catch (Exception e) {
            log.error("Error fetching all tourist types", e);
            throw e;
        }
    }

    public List<Trainer.Specialization> findAllSpecializations() {
        try {
            return trainerDAO.findAllSpecializations();
        } catch (Exception e) {
            log.error("Error fetching all specializations", e);
            throw e;
        }
    }

    public List<Section> findAllSections() {
        try {
            return sectionDAO.findAll();
        } catch (Exception e) {
            log.error("Error fetching all section", e);
            throw e;
        }
    }

    public void saveTourist(Tourist tourist, Contacts contacts) {
        try {
            contacts.setId(contactsDAO.save(contacts));
            tourist.setContacts(contacts);
            Tourist.TouristType touristType = new Tourist.TouristType();
            touristType.setId(defaultTouristTypeId);
            tourist.setType(touristType);
            Integer touristId = touristDAO.save(tourist);

            log.info("Saved tourist with ID: {}", touristId);
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
