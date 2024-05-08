package ru.nsu.skopintsev.turclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.nsu.skopintsev.turclub.dao.TouristDAO;
import ru.nsu.skopintsev.turclub.models.Tourist;

import java.util.List;

@Controller
public class TouristController {

    @Autowired
    private TouristDAO touristDAO;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        List<Tourist> listTourists = touristDAO.findAll();
        model.addAttribute("listTourists", listTourists);
        return "tourists";
    }

    @GetMapping("/new")
    public String showNewTouristForm(Model model) {
        Tourist tourist = new Tourist();
        model.addAttribute("tourist", tourist);
        return "add-tourist";
    }

    @PostMapping("/save")
    public String saveTourist(@ModelAttribute("tourist") Tourist tourist) {
        touristDAO.save(tourist);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEditTouristForm(@PathVariable Long id, Model model) {
        Tourist tourist = touristDAO.findById(id);
        model.addAttribute("tourist", tourist);
        return "edit-tourist";
    }

    @PostMapping("/update/{id}")
    public String updateTourist(@PathVariable Long id, @ModelAttribute("tourist") Tourist tourist) {
        tourist.setId(id);
        touristDAO.update(tourist);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteTourist(@PathVariable Long id) {
        touristDAO.deleteById(id);
        return "redirect:/";
    }
}