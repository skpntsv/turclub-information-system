package ru.nsu.skopintsev.turclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.skopintsev.turclub.dao.ContactsDAO;
import ru.nsu.skopintsev.turclub.dao.SectionDAO;
import ru.nsu.skopintsev.turclub.dao.TouristDAO;
import ru.nsu.skopintsev.turclub.models.Contacts;
import ru.nsu.skopintsev.turclub.models.Tourist;

import java.util.List;

@Controller
@RequestMapping("/tourist")
public class TouristController {
    private final TouristDAO touristDAO;
    private final SectionDAO sectionDAO;
    private final ContactsDAO contactsDAO;

    @Autowired
    public TouristController(SectionDAO sectionDAO, TouristDAO touristDAO, ContactsDAO contactsDAO) {
        this.sectionDAO = sectionDAO;
        this.touristDAO = touristDAO;
        this.contactsDAO = contactsDAO;
    }

    @GetMapping
    public String listTourists(Model model) {
        List<Tourist> tourists = touristDAO.findAll();
        model.addAttribute("listTourists", tourists);
        return "tourist/tourists";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("tourist", new Tourist());
        model.addAttribute("contact", new Contacts());
        return "tourist/add-tourist";
    }

    @PostMapping("/save")
    public String saveTourist(@ModelAttribute("tourist") Tourist tourist,
                              @ModelAttribute("contact") Contacts contacts) {
        contactsDAO.save(contacts);
        touristDAO.save(tourist);
        return "redirect:/tourist";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Tourist tourist = touristDAO.findById(id);
        model.addAttribute("tourist", tourist);
        return "tourist/edit-tourist";
    }

    @PostMapping("/update/{id}")
    public String updateTourist(@PathVariable Long id, @ModelAttribute("tourist") Tourist tourist) {
        tourist.setId(id);
        touristDAO.update(tourist);
        return "redirect:/tourist";
    }

    @GetMapping("/delete/{id}")
    public String deleteTourist(@PathVariable Long id) {
        touristDAO.deleteById(id);
        return "redirect:/tourist";
    }
}