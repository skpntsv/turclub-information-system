package ru.nsu.skopintsev.turclub.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.nsu.skopintsev.turclub.dao.TouristDAO;
import ru.nsu.skopintsev.turclub.models.Hike;
import ru.nsu.skopintsev.turclub.services.HikeService;
import ru.nsu.skopintsev.turclub.services.RouteService;

@Slf4j
@Controller
@RequestMapping("/hike")
public class HikeController {

    private final HikeService hikeService;
    private final RouteService routeService;
    private final TouristDAO touristDAO;

    public HikeController(HikeService hikeService, RouteService routeService, TouristDAO touristDAO) {
        this.hikeService = hikeService;
        this.routeService = routeService;
        this.touristDAO = touristDAO;
    }

    @RequestMapping
    public String listHikes(Model model) {
        model.addAttribute("listHikes", hikeService.findAllHikes());

        return "hike/list-hike";
    }

    @GetMapping("/details/{id}")
    public String detailsHike(@PathVariable Integer id, Model model) {
        model.addAttribute("hike", hikeService.findHikeById(id));
//        model.addAttribute("hikeTypeList", hikeService.findAllHikeTypes());
//        model.addAttribute("routeList", routeService.findAllRoutes());
//        model.addAttribute("instructorList", touristDAO.findAllInstructors());

        return "hike/details-hike";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("hike", new Hike());
        model.addAttribute("hikeTypeList", hikeService.findAllHikeTypes());
        model.addAttribute("routeList", routeService.findAllRoutes());
        model.addAttribute("instructorList", touristDAO.findAllInstructors());

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
        model.addAttribute("routeList", routeService.findAllRoutes());
        model.addAttribute("instructorList", touristDAO.findAllInstructors());


        return "hike/edit-hike";
    }

    @PostMapping("/update/{id}")
    public String updateHike(@PathVariable("id") Integer id,
                             @ModelAttribute("hike") Hike hike,
                             BindingResult bindingResult,
                             Model model) {
        hike.setId(id);
        try {
            hikeService.updateHike(hike);
        } catch (Exception e) {
            log.error("Error updating hike", e);

            model.addAttribute("hike", hikeService.findHikeById(id));
            model.addAttribute("hikeTypeList", hikeService.findAllHikeTypes());
            model.addAttribute("routeList", routeService.findAllRoutes());
            model.addAttribute("instructorList", touristDAO.findAllInstructors());
            model.addAttribute("error", e.getCause().getMessage());
            return "hike/edit-hike";
        }

        return "redirect:/hike";
    }

    @GetMapping("/delete/{id}")
    public String deleteHike(@PathVariable("id") Integer id) {
        hikeService.deleteHikeById(id);

        return "redirect:/hike";
    }
}
