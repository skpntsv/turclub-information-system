package ru.nsu.skopintsev.turclub.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.skopintsev.turclub.dao.quries.GetCompetitionDAO;
import ru.nsu.skopintsev.turclub.dao.quries.GetTouristsDAO;
import ru.nsu.skopintsev.turclub.dao.quries.GetTrainerDAO;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class QueriesService {
    private final GetTouristsDAO getTouristsDAO;
    private final GetTrainerDAO getTrainerDAO;
    private final GetCompetitionDAO getCompetitionDAO;

    @Autowired
    public QueriesService(GetTouristsDAO getTouristsDAO, GetTrainerDAO getTrainerDAO, GetCompetitionDAO getCompetitionDAO) {
        this.getTouristsDAO = getTouristsDAO;
        this.getTrainerDAO = getTrainerDAO;
        this.getCompetitionDAO = getCompetitionDAO;
    }

    public int getCountTouristByCriteria(Integer sectionId, Integer groupId, String gender,
                                         Integer birthdayStartYear, Integer birthdayEndYear,
                                         Integer minAge, Integer maxAge) {
        try {
            return getTouristsDAO.getCountTouristByCriteria(sectionId, groupId, gender, birthdayStartYear, birthdayEndYear, minAge, maxAge);
        } catch (Exception e) {
            log.error("Error execute query[getCountTouristByCriteria]", e);
            throw e;
        }
    }

    public List<Map<String, Object>> getTouristsByCriteria(Integer sectionId, Integer groupId, String gender,
                                                           Integer birthdayStartYear, Integer birthdayEndYear,
                                                           Integer minAge, Integer maxAge) {
        try {
            return getTouristsDAO.getTouristByCriteria(sectionId, groupId, gender, birthdayStartYear, birthdayEndYear, minAge, maxAge);
        } catch (Exception e) {
            log.error("Error execute query[getTouristByCriteria]", e);
            throw e;
        }
    }

    public int getCountTrainerByCriteria(Integer sectionId, String gender,
                                         Integer age, Integer minSalary,
                                         Integer maxSalary, Integer specializationId) {
        try {
            return getTrainerDAO.getCountTrainerByCriteria(sectionId, gender, age, minSalary, maxSalary, specializationId);
        } catch (Exception e) {
            log.error("Error execute query[getCountTrainerByCriteria]", e);
            throw e;
        }
    }

    public List<Map<String, Object>> getTrainersByCriteria(Integer sectionId, String gender,
                                                           Integer age, Integer minSalary,
                                                           Integer maxSalary, Integer specializationId) {
        try {
            return getTrainerDAO.getTrainerByCriteria(sectionId, gender, age, minSalary, maxSalary, specializationId);
        } catch (Exception e) {
            log.error("Error execute query[getTrainerByCriteria]", e);
            throw e;
        }
    }

    public int getCountCompetitionBySection(Integer sectionId) {
        try {
            return getCompetitionDAO.getCountCompetitionBySection(sectionId);
        } catch (Exception e) {
            log.error("Error execute query[getCountCompetitionByCriteria]", e);
            throw e;
        }
    }

    public List<Map<String, Object>> getCompetitionBySection(Integer sectionId) {
        try {
            return getCompetitionDAO.getCompetitionBySection(sectionId);
        } catch (Exception e) {
            log.error("Error execute query[getCompetitionBySection]", e);
            throw e;
        }
    }

    public List<Map<String, Object>> getTrainerByGroupsAndTime(Integer groupId, Date startDate, Date endDate) {
        try {
            return getTrainerDAO.getTrainerByGroupsAndTime(groupId, startDate, endDate);
        } catch (Exception e) {
            log.error("Error execute query[getTrainerByGroupsAndTime]", e);
            throw e;
        }
    }

    public List<Map<String, Object>> getTouristByHike(Integer sectionId, Integer groupId, Integer hikeId, Integer routeId, Integer pointId, Integer maxCategory, Integer minHikes) {
        try {
            return getTouristsDAO.getTouristByHike(sectionId, groupId, hikeId, routeId, pointId, maxCategory, minHikes);
        } catch (Exception e) {
            log.error("Error execute query[getTouristsByHike]", e);
            throw e;
        }
    }

    public int getCountTouristByHike(Integer sectionId, Integer groupId, Integer hikeId, Integer routeId, Integer pointId, Integer maxCategory, Integer minHikes) {
        try {
            return getTouristsDAO.getCountTouristByHike(sectionId, groupId, hikeId, routeId, pointId, maxCategory, minHikes);
        } catch (Exception e) {
            log.error("Error execute query[getCountTouristByHike]", e);
            throw e;
        }
    }
}
