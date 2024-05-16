package ru.nsu.skopintsev.turclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.skopintsev.turclub.models.Contacts;
import ru.nsu.skopintsev.turclub.models.Tourist;
import ru.nsu.skopintsev.turclub.services.TouristService;

import java.util.List;

@Controller
@RequestMapping("/tourist")
public class TouristController {
    private final TouristService touristService;

    @Autowired
    public TouristController(TouristService touristService) {
        this.touristService = touristService;
    }

    @GetMapping
    public String listTourists(Model model) {
        List<Tourist> tourists = touristService.findAllTourists();
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
        touristService.saveTourist(tourist, contacts);
        return "redirect:/tourist";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Tourist tourist = touristService.findTouristById(id);
        model.addAttribute("tourist", tourist);
        return "tourist/edit-tourist";
    }

    @PostMapping("/update/{id}")
    public String updateTourist(@PathVariable Integer id, @ModelAttribute("tourist") Tourist tourist) {
        tourist.setId(id);
        touristService.updateTourist(tourist);
        return "redirect:/tourist";
    }

    @GetMapping("/delete/{id}")
    public String deleteTourist(@PathVariable Integer id) {
        touristService.deleteTouristById(id);
        return "redirect:/tourist";
    }
}
