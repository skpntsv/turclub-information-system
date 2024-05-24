package ru.nsu.skopintsev.turclub.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.nsu.skopintsev.turclub.models.Hike;
import ru.nsu.skopintsev.turclub.services.HikeService;

@Slf4j
@Controller
@RequestMapping("/hike")
public class HikeController {

    private final HikeService hikeService;

    public HikeController(HikeService hikeService) {
        this.hikeService = hikeService;
    }

    @RequestMapping
    public String listHikes(Model model) {
        model.addAttribute("listHikes", hikeService.findAllHikes());

        return "hike/list-hike";
    }

    @GetMapping("/details/{id}")
    public String detailsHike(@PathVariable Integer id, Model model) {
        model.addAttribute("hike", hikeService.findHikeById(id));

        return "hike/details-hike";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("hike", new Hike());

        return "hike/add-hike";
    }

    @PostMapping("/save")
    public String saveHike(@ModelAttribute("hike") Hike hike) {
        hikeService.saveHike(hike);

        return "redirect:/hike";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id,
                               Model model) {
        model.addAttribute("hike", hikeService.findHikeById(id));
        model.addAttribute("hikeTypeList", hikeService.findAllHikeTypes());


        return "hike/edit-hike";
    }

    @PostMapping("/update/{id}")
    public String updateHike(@PathVariable("id") Integer id,
                             @ModelAttribute("hike") Hike hike,
                             BindingResult bindingResult) {
        hike.setId(id);
        hikeService.updateHike(hike);

        return "redirect:/hike";
    }

    @GetMapping("/delete/{id}")
    public String deleteHike(@PathVariable("id") Integer id) {
        hikeService.deleteHikeById(id);

        return "redirect:/hike";
    }
}
