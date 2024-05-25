package ru.nsu.skopintsev.turclub.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.skopintsev.turclub.dao.RouteDAO;
import ru.nsu.skopintsev.turclub.models.Route;

import java.util.List;

@Service
@Transactional
@Slf4j
public class RouteService {
    private final RouteDAO routeDAO;

    @Autowired
    public RouteService(RouteDAO routeDAO) {
        this.routeDAO = routeDAO;
    }

    public List<Route> findAllRoutes() {
        try {
            return routeDAO.findAll();
        } catch (Exception e) {
            log.error("Error fetching all routes", e);
            throw e;
        }
    }
}
