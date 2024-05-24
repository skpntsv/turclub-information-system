package ru.nsu.skopintsev.turclub.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.skopintsev.turclub.dao.quries.GetTouristsDAO;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class QueriesService {
    private final GetTouristsDAO getTouristsDAO;

    @Autowired
    public QueriesService(GetTouristsDAO getTouristsDAO) {
        this.getTouristsDAO = getTouristsDAO;
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
}
