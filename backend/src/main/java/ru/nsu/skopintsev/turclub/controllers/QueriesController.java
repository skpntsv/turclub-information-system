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

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final HikeService hikeService;
    private final RouteService routeService;
    private final CheckPointService checkPointService;
    private final SectionService sectionService;

    @Autowired
    public QueriesController(QueriesService queriesService, TouristService touristService, GroupsService groupsService, HikeService hikeService, RouteService routeService, CheckPointService checkPointService, SectionService sectionService) {
        this.queriesService = queriesService;
        this.touristService = touristService;
        this.groupsService = groupsService;
        this.hikeService = hikeService;
        this.routeService = routeService;
        this.checkPointService = checkPointService;
        this.sectionService = sectionService;
    }

    @GetMapping
    public String index() {
        return "query";
    }


    @GetMapping("/1")
    public String getTouristsByCriteria(Model model) {
        model.addAttribute("sectionList", sectionService.findAllSections());
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
        List<Map<String, Object>> lines = queriesService.getTouristsByCriteria(sectionId, groupId, gender, birthdayStartYear, birthdayEndYear, minAge, maxAge);

        model.addAttribute("count", count);
        model.addAttribute("lines", lines);
        model.addAttribute("headers", lines.isEmpty() ? List.of() : new ArrayList<>(lines.get(0).keySet()));

        log.info("Executed query: 1");

        model.addAttribute("sectionList", sectionService.findAllSections());
        model.addAttribute("groupList", groupsService.findAllGroups());
        return "queries/1";
    }

    @GetMapping("/2")
    public String getTrainersByCriteria(Model model) {
        model.addAttribute("sectionList", sectionService.findAllSections());
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

        model.addAttribute("sectionList", sectionService.findAllSections());
        model.addAttribute("specializationList", touristService.findAllSpecializations());
        return "queries/2";
    }

    // 3
    @GetMapping("/3")
    public String getCompetitionsBySection(Model model) {
        model.addAttribute("sectionList", sectionService.findAllSections());
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

        model.addAttribute("sectionList", sectionService.findAllSections());
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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Model model) {
        log.info("Executing query: 4");

        Timestamp startTimestamp = startDate != null ? Timestamp.valueOf(startDate) : null;
        Timestamp endTimestamp = endDate != null ? Timestamp.valueOf(endDate) : null;
        List<Map<String, Object>> trainers = queriesService.getTrainerByGroupsAndTime(groupId, startTimestamp, endTimestamp);
        model.addAttribute("trainers", trainers);
        model.addAttribute("headers", trainers.isEmpty() ? List.of() : new ArrayList<>(trainers.get(0).keySet()));

        log.info("Executed query: 4");

        model.addAttribute("groupList", groupsService.findAllGroups());
        return "queries/4";
    }

    // 5
    @GetMapping("/5")
    public String getTrainerBySpecializationAndTime(Model model) {
        model.addAttribute("sectionList", sectionService.findAllSections());
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

        model.addAttribute("sectionList", sectionService.findAllSections());
        model.addAttribute("groupList", groupsService.findAllGroups());
        model.addAttribute("hikeList", hikeService.findAllHikes());
        model.addAttribute("routeList", routeService.findAllRoutes());
        model.addAttribute("checkpointList", checkPointService.findAllCheckpoints());
        return "queries/5";
    }

    @GetMapping("/6")
    public String getSuperVisorByCriteria(Model model) {

        return "queries/6";
    }

    @PostMapping("/6")
    public String getSuperVisorByCriteria(
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary,
            @RequestParam(required = false) Integer minBirthYear,
            @RequestParam(required = false) Integer maxBirthYear,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minHireDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxHireDate,
            Model model) {
        log.info("Executing query: 6");

        Date minHireDateSql = minHireDate != null ? Date.valueOf(minHireDate) : null;
        Date maxHireDateSql = maxHireDate != null ? Date.valueOf(maxHireDate) : null;

        List<Map<String, Object>> superVisors = queriesService.getSuperVisorByCriteria(minSalary, maxSalary,
                minBirthYear, maxBirthYear, minHireDateSql, maxHireDateSql);
        model.addAttribute("lines", superVisors);
        model.addAttribute("headers", superVisors.isEmpty() ? List.of() : new ArrayList<>(superVisors.get(0).keySet()));

        log.info("Executed query: 6");

        return "queries/6";
    }

    @GetMapping("/7")
    public String getScheduleTrainersByCriteria(Model model) {
        model.addAttribute("trainerList", sectionService.findAllTrainers());
        model.addAttribute("sectionList", sectionService.findAllSections());
        return "queries/7";
    }

    @PostMapping("/7")
    public String getScheduleTrainersByCriteria(
            @RequestParam(required = false) Integer trainerId,
            @RequestParam(required = false) Integer sectionId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {
        log.info("Executing query: 7");

        Date startDateSql = startDate != null ? Date.valueOf(startDate) : null;
        Date endDateSql = endDate != null ? Date.valueOf(endDate) : null;

        List<Map<String, Object>> lines = queriesService.getScheduleTrainersByCriteria(trainerId, sectionId, startDateSql, endDateSql);

        model.addAttribute("lines", lines);
        model.addAttribute("headers", lines.isEmpty() ? List.of() : new ArrayList<>(lines.get(0).keySet()));

        log.info("Executed query: 7");
        return "queries/7";
    }

    @GetMapping("/8")
    public String getRouteByCriteria(Model model) {
        model.addAttribute("sectionList", sectionService.findAllSections());
        model.addAttribute("instructorList", touristService.findAllInstructors());
        return "queries/8";
    }

    @PostMapping("/8")
    public String getRouteByCriteria(
            @RequestParam(required = false) Integer instructorId,
            @RequestParam(required = false) Integer sectionId,
            @RequestParam(required = false) Integer groupCount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate,
            Model model) {
        log.info("Executing query: 8");

        Timestamp startTimestamp = startDate != null ? Timestamp.valueOf(startDate) : null;
        Timestamp endTimestamp = endDate != null ? Timestamp.valueOf(endDate) : null;

        List<Map<String, Object>> lines = queriesService.getRouteByCriteria(sectionId, instructorId, groupCount, startTimestamp, endTimestamp);
        int count = lines.size();
        model.addAttribute("count", count);

        model.addAttribute("lines", lines);
        model.addAttribute("headers", lines.isEmpty() ? List.of() : new ArrayList<>(lines.get(0).keySet()));

        log.info("Executed query: 8");

        model.addAttribute("sectionList", sectionService.findAllSections());
        model.addAttribute("instructorList", touristService.findAllInstructors());
        return "queries/8";
    }

    @GetMapping("/9")
    public String getRouteByCriteria9(Model model) {
        model.addAttribute("checkpointList", checkPointService.findAllCheckpoints());
        return "queries/9";
    }

    @PostMapping("/9")
    public String getRouteByCriteria9(
            @RequestParam(required = false) Integer checkpointId,
            @RequestParam(required = false) Integer length,
            @RequestParam(required = false) Integer category,
            Model model) {
        log.info("Executing query: 9");

        List<Map<String, Object>> lines = queriesService.getRouteByCriteria(checkpointId, length, category);
        int count = lines.size();
        model.addAttribute("count", count);

        model.addAttribute("lines", lines);
        model.addAttribute("headers", lines.isEmpty() ? List.of() : new ArrayList<>(lines.get(0).keySet()));

        log.info("Executed query: 9");

        model.addAttribute("checkpointList", checkPointService.findAllCheckpoints());
        return "queries/9";
    }

    @GetMapping("/10")
    public String getTouristByHike(Model model) {
        model.addAttribute("sectionList", sectionService.findAllSections());
        return "queries/10";
    }

    @PostMapping("/10")
    public String getTouristByHike(
            @RequestParam(required = false) Integer sectionId,
            @RequestParam(required = false) Integer groupId,
            @RequestParam(required = false) Integer skill,
            Model model) {
        log.info("Executing query: 10");

        List<Map<String, Object>> lines = queriesService.getTouristByHike(sectionId, groupId, skill);
        int count = lines.size();
        model.addAttribute("count", count);

        model.addAttribute("lines", lines);
        model.addAttribute("headers", lines.isEmpty() ? List.of() : new ArrayList<>(lines.get(0).keySet()));

        log.info("Executed query: 10");

        model.addAttribute("sectionList", sectionService.findAllSections());
        return "queries/10";
    }

    @GetMapping("/11")
    public String getInstructorByCriteria(Model model) {
        model.addAttribute("touristTypeList", touristService.findAllTouristTypes());
        model.addAttribute("checkpointList", checkPointService.findAllCheckpoints());
        model.addAttribute("hikeList", hikeService.findAllHikes());
        model.addAttribute("routeList", routeService.findAllRoutes());
        return "queries/11";
    }

    @PostMapping("/11")
    public String getInstructorByCriteria(
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) Integer touristType,
            @RequestParam(required = false) Integer hikeCount,
            @RequestParam(required = false) Integer hikeId,
            @RequestParam(required = false) Integer routeId,
            @RequestParam(required = false) Integer checkpointId,
            Model model) {
        log.info("Executing query: 11");

        List<Map<String, Object>> lines = queriesService.getInstructorByCriteria(category, touristType, hikeCount, hikeId, routeId, checkpointId);
        int count = lines.size();
        model.addAttribute("count", count);

        model.addAttribute("lines", lines);
        model.addAttribute("headers", lines.isEmpty() ? List.of() : new ArrayList<>(lines.get(0).keySet()));

        log.info("Executed query: 11");

        model.addAttribute("touristTypeList", touristService.findAllTouristTypes());
        model.addAttribute("checkpointList", checkPointService.findAllCheckpoints());
        model.addAttribute("hikeList", hikeService.findAllHikes());
        model.addAttribute("routeList", routeService.findAllRoutes());
        return "queries/11";
    }
}
