package ru.nsu.skopintsev.turclub.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.skopintsev.turclub.dao.CheckPointDAO;
import ru.nsu.skopintsev.turclub.models.CheckPoint;

import java.util.List;

@Service
@Transactional
@Slf4j
public class CheckPointService {

    private final CheckPointDAO checkPointDAO;

    @Autowired
    public CheckPointService(CheckPointDAO checkPointDAO) {
        this.checkPointDAO = checkPointDAO;
    }

    public List<CheckPoint> findAllCheckpoints() {
        return checkPointDAO.findAll();
    }
}
