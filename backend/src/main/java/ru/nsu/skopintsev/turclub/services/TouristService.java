package ru.nsu.skopintsev.turclub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.skopintsev.turclub.dao.ContactsDAO;
import ru.nsu.skopintsev.turclub.dao.SectionDAO;
import ru.nsu.skopintsev.turclub.dao.TouristDAO;
import ru.nsu.skopintsev.turclub.models.Contacts;
import ru.nsu.skopintsev.turclub.models.Section;
import ru.nsu.skopintsev.turclub.models.Tourist;

import java.util.List;

@Service
@Transactional
public class TouristService {
    private final TouristDAO touristDAO;
    private final SectionDAO sectionDAO;
    private final ContactsDAO contactsDAO;

    @Autowired
    public TouristService(TouristDAO touristDAO, SectionDAO sectionDAO, ContactsDAO contactsDAO) {
        this.touristDAO = touristDAO;
        this.sectionDAO = sectionDAO;
        this.contactsDAO = contactsDAO;
    }

    public List<Section> findAllSections() {
        return sectionDAO.findAll();
    }

    public void saveTourist(Tourist tourist, Contacts contacts) {
        // Сохраняем контактные данные
        contactsDAO.save(contacts);
        // Связываем туриста с контактами
        tourist.setContactId(contacts.getId());
        // Сохраняем туриста
        touristDAO.save(tourist);
    }
}
