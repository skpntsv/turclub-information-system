package ru.nsu.skopintsev.turclub.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import ru.nsu.skopintsev.turclub.services.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/query")
public class QueriesController {
    private final QueriesService queriesService;
    private final TouristService touristService;
    private final GroupsService groupsService;
    private final HikeService hikeService;
    private final RouteService routeService;
    private final CheckPointService checkPointService;

    @Autowired
    public QueriesController(QueriesService queriesService, TouristService touristService, GroupsService groupsService, HikeService hikeService, RouteService routeService, CheckPointService checkPointService) {
        this.queriesService = queriesService;
        this.touristService = touristService;
        this.groupsService = groupsService;
        this.hikeService = hikeService;
        this.routeService = routeService;
        this.checkPointService = checkPointService;
    }

    @GetMapping
    public String index() {
        return "query";
    }


    @GetMapping("/1")
    public String getTouristsByCriteria(Model model) {
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

        model.addAttribute("sectionList", touristService.findAllSections());
        model.addAttribute("groupList", groupsService.findAllGroups());
        return "queries/1";
    }

    @GetMapping("/2")
    public String getTrainersByCriteria(Model model) {
        model.addAttribute("sectionList", touristService.findAllSections());
        model.addAttribute("specializationList", touristService.findAllSpecializations());
        return "queries/2";
    }

    @PostMapping("/2")
    public String getTrainersByCriteria(
            @RequestParam(required = false) Integer sectionId,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) Integer minSalary,
            @RequestParam(required = false) Integer maxSalary,
            @RequestParam(required = false) Integer specializationId,
            Model model) {
        log.info("Executing query: 2");
        int count = queriesService.getCountTrainerByCriteria(sectionId, gender, age, minSalary, maxSalary, specializationId);
        List<Map<String, Object>> trainers = queriesService.getTrainersByCriteria(sectionId, gender, age, minSalary, maxSalary, specializationId);
        model.addAttribute("count", count);
        model.addAttribute("trainers", trainers);
        model.addAttribute("headers", trainers.isEmpty() ? List.of() : new ArrayList<>(trainers.get(0).keySet()));

        log.info("Executed query: 2");

        model.addAttribute("sectionList", touristService.findAllSections());
        model.addAttribute("specializationList", touristService.findAllSpecializations());
        return "queries/2";
    }

    // 3
    @GetMapping("/3")
    public String getCompetitionsBySection(Model model) {
        model.addAttribute("sectionList", touristService.findAllSections());
        return "queries/3";
    }

    @PostMapping("/3")
    public String getCompetitionsBySection(
            @RequestParam(required = false) Integer sectionId,
            Model model) {
        log.info("Executing query: 3");
        List<Map<String, Object>> competitions = queriesService.getCompetitionBySection(sectionId);
        int count = queriesService.getCountCompetitionBySection(sectionId);
        model.addAttribute("count", count);
        model.addAttribute("competitions", competitions);
        model.addAttribute("headers", competitions.isEmpty() ? List.of() : new ArrayList<>(competitions.get(0).keySet()));

        log.info("Executed query: 3");

        model.addAttribute("sectionList", touristService.findAllSections());
        return "queries/3";
    }

    // 4
    @GetMapping("/4")
    public String getTrainerByGroupsAndTime(Model model) {
        model.addAttribute("groupList", groupsService.findAllGroups());
        return "queries/4";
    }

    @PostMapping("/4")
    public String getTrainerByGroupsAndTime(
            @RequestParam(required = false) Integer groupId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDateParam,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDateParam,
            Model model) {
        log.info("Executing query: 4");

        java.sql.Date startDate = null, endDate = null;
        if (startDateParam != null) {
            startDate = new java.sql.Date(startDateParam.getTime());
        }
        if (endDateParam != null) {
            endDate = new java.sql.Date(endDateParam.getTime());
        }
        List<Map<String, Object>> trainers = queriesService.getTrainerByGroupsAndTime(groupId, startDate, endDate);
        model.addAttribute("trainers", trainers);
        model.addAttribute("headers", trainers.isEmpty() ? List.of() : new ArrayList<>(trainers.get(0).keySet()));

        log.info("Executed query: 4");

        model.addAttribute("groupList", groupsService.findAllGroups());
        return "queries/4";
    }

    // 5
    @GetMapping("/5")
    public String getTrainerBySpecializationAndTime(Model model) {
        model.addAttribute("sectionList", touristService.findAllSections());
        model.addAttribute("groupList", groupsService.findAllGroups());
        model.addAttribute("hikeList", hikeService.findAllHikes());
        model.addAttribute("routeList", routeService.findAllRoutes());
        model.addAttribute("checkpointList", checkPointService.findAllCheckpoints());
        return "queries/5";
    }

    @PostMapping("/5")
    public String getTrainerBySpecializationAndTime(
            @RequestParam(required = false) Integer sectionId,
            @RequestParam(required = false) Integer groupId,
            @RequestParam(required = false) Integer hikeId,
            @RequestParam(required = false) Integer routeId,
            @RequestParam(required = false) Integer pointId,
            @RequestParam(required = false) Integer maxCategory,
            @RequestParam(required = false) Integer minHikes,
            Model model) {
        log.info("Executing query: 5");

        List<Map<String, Object>> tourists = queriesService.getTouristByHike(sectionId, groupId, hikeId, routeId, pointId, maxCategory, minHikes);
        int count = queriesService.getCountTouristByHike(sectionId, groupId, hikeId, routeId, pointId, maxCategory, minHikes);
        model.addAttribute("count", count);
        model.addAttribute("tourists", tourists);
        model.addAttribute("headers", tourists.isEmpty() ? List.of() : new ArrayList<>(tourists.get(0).keySet()));

        log.info("Executed query: 5");

        model.addAttribute("sectionList", touristService.findAllSections());
        model.addAttribute("groupList", groupsService.findAllGroups());
        model.addAttribute("hikeList", hikeService.findAllHikes());
        model.addAttribute("routeList", routeService.findAllRoutes());
        model.addAttribute("checkpointList", checkPointService.findAllCheckpoints());
        return "queries/5";
    }
}
