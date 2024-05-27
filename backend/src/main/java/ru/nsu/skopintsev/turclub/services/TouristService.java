package ru.nsu.skopintsev.turclub.services;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.skopintsev.turclub.dao.ContactsDAO;
import ru.nsu.skopintsev.turclub.dao.SectionDAO;
import ru.nsu.skopintsev.turclub.dao.TouristDAO;
import ru.nsu.skopintsev.turclub.dao.TrainerDAO;
import ru.nsu.skopintsev.turclub.models.*;

import java.util.List;
import java.util.Optional;

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

    @Value("${error.trainer_tourist_type_not_fount}")
    private String trainerTouristTypeNotFoundMessage;

    @Value("${tourist.trainer.type}")
    private String trainerName;

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

    public Optional<Trainer> findTrainerById(Integer id) {
        try {
            Trainer trainer = trainerDAO.findById(id);
            return trainer != null ? Optional.of(trainer) : Optional.empty();
        } catch (EmptyResultDataAccessException e) {
            log.info("No trainer found with ID: {}", id);

            return Optional.empty();
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

    public void saveTourist(Tourist tourist) {
        try {
            Contacts contacts = tourist.getContacts();
            contacts.setId(contactsDAO.save(tourist.getContacts()));
            tourist.setContacts(contacts);

            // Установка типа туриста по умолчанию (из staff.properties)
            Tourist.TouristType touristType = new Tourist.TouristType();
            touristType.setId(defaultTouristTypeId);
            tourist.setType(touristType);

            // Сохранение туриста со всеми параметрами
            Integer touristId = touristDAO.save(tourist);

            log.info("Saved tourist with ID: {}", touristId);
        } catch (Exception e) {
            log.error("Error saving tourist", e);
            throw e;
        }
    }

    public void updateTourist(Tourist tourist, Trainer trainer) {
        int touristId;
        try {
            List<Tourist.TouristType> touristTypes = touristDAO.findAllTouristType();
            Optional<Integer> trainerTypeId = touristTypes.stream()
                    .filter(type -> trainerName.equals(type.getName()))
                    .map(Tourist.TouristType::getId)
                    .findFirst();
            if (trainerTypeId.isEmpty()) {
                log.error("Type '{}' not exist", trainerName);
                throw new IllegalStateException(trainerTouristTypeNotFoundMessage);
            }
            if (tourist.getType().getId().equals(trainerTypeId.get())) {
                trainer.setId(tourist.getId());
                if (getTrainer(tourist.getId()).isPresent()) {
                    trainerDAO.update(trainer);
                } else {
                    trainerDAO.save(trainer);
                }

                touristDAO.update(tourist);
                contactsDAO.update(tourist.getContacts());
                log.info("Updated tourist and subtype Trainer with ID: {}", tourist.getId());
            } else {
                touristDAO.update(tourist);
                contactsDAO.update(tourist.getContacts());
                log.info("Updated tourist with ID: {}", tourist.getId());
            }
        } catch (Exception e) {
            log.error("Error updating tourist", e);
            throw e;
        }
    }

    private Optional<Trainer> getTrainer(Integer id) {
        try {
            Trainer trainer = trainerDAO.findById(id);
            return trainer != null ? Optional.of(trainer) : Optional.empty();
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
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

    public List<Tourist> findAllInstructors() {
        try {
            return touristDAO.findAllInstructors();
        } catch (Exception e) {
            log.error("Error fetching all instructors", e);
            throw e;
        }
    }
}
