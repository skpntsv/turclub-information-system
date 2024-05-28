package ru.nsu.skopintsev.turclub.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.skopintsev.turclub.dao.SectionDAO;
import ru.nsu.skopintsev.turclub.dao.SuperVisorDAO;
import ru.nsu.skopintsev.turclub.dao.TrainerDAO;
import ru.nsu.skopintsev.turclub.models.Section;
import ru.nsu.skopintsev.turclub.models.SuperVisor;
import ru.nsu.skopintsev.turclub.models.Tourist;
import ru.nsu.skopintsev.turclub.models.Trainer;

import java.util.List;

@Service
@Transactional
@Slf4j
public class SectionService {
    private final SectionDAO sectionDAO;
    private final SuperVisorDAO superVisorDAO;
    private final TrainerDAO trainerDAO;

    @Autowired
    public SectionService(SectionDAO sectionDAO, SuperVisorDAO superVisorDAO, TrainerDAO trainerDAO) {
        this.sectionDAO = sectionDAO;
        this.superVisorDAO = superVisorDAO;
        this.trainerDAO = trainerDAO;
    }

    public List<Section> findAllSections() {
        try {
            return sectionDAO.findAll();
        } catch (Exception e) {
            log.error("Error fetching all section", e);
            throw e;
        }
    }

    public List<SuperVisor> findAllSupervisors() {
        try {
            return superVisorDAO.findAll();
        } catch (Exception e) {
            log.error("Error fetching all supervisors", e);
            throw e;
        }
    }

    public List<Trainer> findAllTrainers() {
        try {
            return trainerDAO.findAll();
        } catch (Exception e) {
            log.error("Error fetching all trainers", e);
            throw e;
        }
    }
}
