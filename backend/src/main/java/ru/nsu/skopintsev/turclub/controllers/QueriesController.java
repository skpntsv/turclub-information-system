package ru.nsu.skopintsev.turclub.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import ru.nsu.skopintsev.turclub.services.GroupsService;
import ru.nsu.skopintsev.turclub.services.QueriesService;
import ru.nsu.skopintsev.turclub.services.TouristService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/query")
public class QueriesController {
    private final QueriesService queriesService;
    private final TouristService touristService;
    private final GroupsService groupsService;

    @Autowired
    public QueriesController(QueriesService queriesService, TouristService touristService, GroupsService groupsService) {
        this.queriesService = queriesService;
        this.touristService = touristService;
        this.groupsService = groupsService;
    }

    @GetMapping
    public String index() {
        return "query";
    }


    @GetMapping("/1")
    public String getTouristsByCriteria1(Model model) {
        model.addAttribute("sectionList", touristService.findAllSections());
        model.addAttribute("groupList", groupsService.findAllGroups());
        return "queries/1";
    }

    @PostMapping("/1")
    public String getTouristsByCriteria(
            @RequestParam(required = false) Integer sectionId,
            @RequestParam(required = false) Integer groupId,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer birthdayStartYear,
            @RequestParam(required = false) Integer birthdayEndYear,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            Model model) {

        log.info("Executing query: 1");
        int count = queriesService.getCountTouristByCriteria(sectionId, groupId, gender, birthdayStartYear, birthdayEndYear, minAge, maxAge);
        List<Map<String, Object>> tourists = queriesService.getTouristsByCriteria(sectionId, groupId, gender, birthdayStartYear, birthdayEndYear, minAge, maxAge);

        model.addAttribute("count", count);
        model.addAttribute("tourists", tourists);
        model.addAttribute("headers", tourists.isEmpty() ? List.of() : new ArrayList<>(tourists.get(0).keySet()));

        log.info("Executed query: 1");
        return "queries/1";
    }
}
