package ru.nsu.skopintsev.turclub.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.skopintsev.turclub.dao.SectionDAO;
import ru.nsu.skopintsev.turclub.dao.SuperVisorDAO;
import ru.nsu.skopintsev.turclub.models.Section;
import ru.nsu.skopintsev.turclub.models.SuperVisor;

import java.util.List;

@Service
@Transactional
@Slf4j
public class SectionService {
    private final SectionDAO sectionDAO;
    private final SuperVisorDAO superVisorDAO;

    @Autowired
    public SectionService(SectionDAO sectionDAO, SuperVisorDAO superVisorDAO) {
        this.sectionDAO = sectionDAO;
        this.superVisorDAO = superVisorDAO;
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
}
