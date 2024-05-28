package ru.nsu.skopintsev.turclub.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.nsu.skopintsev.turclub.models.Section;
import ru.nsu.skopintsev.turclub.models.Tourist;
import ru.nsu.skopintsev.turclub.models.Trainer;
import ru.nsu.skopintsev.turclub.services.SectionService;
import ru.nsu.skopintsev.turclub.services.TouristService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/tourist")
public class TouristController {
    private final TouristService touristService;
    private final SectionService sectionService;

    @Value("${tourist.trainer.type}")
    private String trainerName;

    @Autowired
    public TouristController(TouristService touristService, SectionService sectionService) {
        this.touristService = touristService;
        this.sectionService = sectionService;
    }

    @GetMapping
    public String listTourists(Model model) {
        List<Tourist> tourists = touristService.findAllTourists();
        model.addAttribute("listTourists", tourists);

        return "tourist/list-tourist";
    }

    @GetMapping("/details/{id}")
    public String detailsTourist(@PathVariable Integer id, Model model) {
        Tourist tourist = touristService.findTouristById(id);
        model.addAttribute("tourist", tourist);

        if (tourist.getType().getName().equals(trainerName)) {
            Optional<Trainer> trainer = touristService.findTrainerById(tourist.getId());
            if (trainer.isPresent()) {
                model.addAttribute("trainer", trainer.get());
            } else {
                model.addAttribute("error", "Турист имеет тип 'Тренер', но отсутствует запись в таблице Trainer.");
                model.addAttribute("suggestFix", true);
            }
        }

        return "tourist/details-tourist";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("tourist", new Tourist());

        return "tourist/add-tourist";
    }

    @PostMapping("/save")
    public String saveTourist(@ModelAttribute("tourist") Tourist tourist) {
        touristService.saveTourist(tourist);

        return "redirect:/tourist";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Tourist tourist = touristService.findTouristById(id);
        List<Tourist.TouristType> touristTypes = touristService.findAllTouristTypes();
        model.addAttribute("tourist", tourist);
        model.addAttribute("touristTypes", touristTypes);

        // Подтипы
        Optional<Trainer> trainer = touristService.findTrainerById(tourist.getId());
        if (trainer.isPresent()) {
            model.addAttribute("trainer", trainer.get());
        } else {
            model.addAttribute("trainer", new Trainer());
        }

        List<Trainer.Specialization> specialization = touristService.findAllSpecializations();
        List<Section> sections = sectionService.findAllSections();
        model.addAttribute("specializationList", specialization);
        model.addAttribute("sectionList", sections);

        return "tourist/edit-tourist";
    }

    @PostMapping("/update/{id}")
    public String updateTourist(@PathVariable Integer id,
                                @ModelAttribute("tourist") Tourist tourist,
                                @ModelAttribute("trainer") Trainer trainer,
                                BindingResult bindingResult) {
        tourist.setId(id);
        touristService.updateTourist(tourist, trainer);
        return "redirect:/tourist";
    }

    @GetMapping("/delete/{id}")
    public String deleteTourist(@PathVariable Integer id) {
        touristService.deleteTouristById(id);

        return "redirect:/tourist";
    }
}
