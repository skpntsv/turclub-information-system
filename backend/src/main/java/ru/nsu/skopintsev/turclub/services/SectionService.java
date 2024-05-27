package ru.nsu.skopintsev.turclub.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.skopintsev.turclub.dao.SectionDAO;
import ru.nsu.skopintsev.turclub.models.Section;

import java.util.List;

@Service
@Transactional
@Slf4j
public class SectionService {
    private final SectionDAO sectionDAO;

    @Autowired
    public SectionService(SectionDAO sectionDAO) {
        this.sectionDAO = sectionDAO;
    }

    public List<Section> findAllSections() {
        try {
            return sectionDAO.findAll();
        } catch (Exception e) {
            log.error("Error fetching all section", e);
            throw e;
        }
    }
}
