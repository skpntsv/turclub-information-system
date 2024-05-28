package ru.nsu.skopintsev.turclub.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.skopintsev.turclub.dao.quries.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class QueriesService {
    private final GetTouristsDAO getTouristsDAO;
    private final GetTrainerDAO getTrainerDAO;
    private final GetCompetitionDAO getCompetitionDAO;
    private final GetSuperVisorDAO getSuperVisorDAO;
    private final GetRouteDAO getRouteDAO;

    @Autowired
    public QueriesService(GetTouristsDAO getTouristsDAO, GetTrainerDAO getTrainerDAO, GetCompetitionDAO getCompetitionDAO, GetSuperVisorDAO getSuperVisorDAO, GetRouteDAO getRouteDAO) {
        this.getTouristsDAO = getTouristsDAO;
        this.getTrainerDAO = getTrainerDAO;
        this.getCompetitionDAO = getCompetitionDAO;
        this.getSuperVisorDAO = getSuperVisorDAO;
        this.getRouteDAO = getRouteDAO;
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

    public List<Map<String, Object>> getTrainerByGroupsAndTime(Integer groupId, Timestamp startDate, Timestamp endDate) {
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

    public List<Map<String, Object>> getSuperVisorByCriteria(Double minSalary, Double maxSalary, Integer minBirthYear,
                                                             Integer maxBirthYear, Date minHireDate, Date maxHireDate) {
        try {
            return getSuperVisorDAO.getSuperVisorByCriteria(minSalary, maxSalary, minBirthYear, maxBirthYear, minHireDate, maxHireDate);
        } catch (Exception e) {
            log.error("Error execute query[getSuperVisorByCriteria]", e);
            throw e;
        }
    }

    public List<Map<String, Object>> getScheduleTrainersByCriteria(Integer trainerId, Integer sectionId, Date startDate, Date endDate) {
        try {
            return getTrainerDAO.getScheduleTrainerByCriteria(trainerId, sectionId, startDate, endDate);
        } catch (Exception e) {
            log.error("Error execute query[getScheduleTrainerByCriteria]", e);
            throw e;
        }
    }

    public List<Map<String, Object>> getRouteByCriteria(Integer sectionId, Integer instructorId, Integer groupCount, Timestamp startTimestamp, Timestamp endTimestamp) {
        try {
            return getRouteDAO.getRouteByCriteria(sectionId, instructorId, groupCount, startTimestamp, endTimestamp);
        } catch (Exception e) {
            log.error("Error execute query[getRouteByCriteria]", e);
            throw e;
        }
    }

    public List<Map<String, Object>> getRouteByCriteria(Integer checkpointId, Integer length, Integer category) {
        try {
            return getRouteDAO.getRouteByCriteria(checkpointId, length, category);
        } catch (Exception e) {
            log.error("Error execute query[getRouteByCriteria]", e);
            throw e;
        }
    }

    public List<Map<String, Object>> getTouristByHike(Integer sectionId, Integer groupId, Integer skill) {
        try {
            return getTouristsDAO.getTouristByHike(sectionId, groupId, skill);
        } catch (Exception e) {
            log.error("Error execute query[getTouristsByHike]", e);
            throw e;
        }
    }

    public List<Map<String, Object>> getInstructorByCriteria(Integer category, Integer touristType, Integer hikeCount, Integer hikeId, Integer routeId, Integer checkpointId) {
        try {
            return getTouristsDAO.getInstructorByCriteria(category, touristType, hikeCount, hikeId, routeId, checkpointId);
        } catch (Exception e) {
            log.error("Error execute query[getInstructorByCriteria]", e);
            throw e;
        }
    }

    public List<Map<String, Object>> getTouristByInstructor(Integer groupId, Integer sectionId) {
        try {
            return getTouristsDAO.getTouristByInstructor(groupId, sectionId);
        } catch (Exception e) {
            log.error("Error execute query[getTouristByInstructor]", e);
            throw e;
        }
    }
}
