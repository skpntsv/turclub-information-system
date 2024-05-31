package ru.nsu.skopintsev.turclub.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.skopintsev.turclub.dao.HikeDAO;
import ru.nsu.skopintsev.turclub.models.Hike;

import java.util.List;

@Service
@Transactional
@Slf4j
public class HikeService {
    private final HikeDAO hikeDAO;

    @Autowired
    public HikeService(HikeDAO hikeDAO) {
        this.hikeDAO = hikeDAO;
    }

    public List<Hike> findAllHikes() {
        try {
            return hikeDAO.findAll();
        } catch (Exception e) {
            log.error("Error fetching all hikes", e);
            throw e;
        }
    }

    public Hike findHikeById(Integer id) {
        try {
            return hikeDAO.findById(id);
        } catch (Exception e) {
            log.error("Error fetching hike by ID: {}", id, e);
            throw e;
        }
    }

    public void saveHike(Hike hike) {
        try {
            hike.setIsPlanned(false);
            hikeDAO.save(hike);
        } catch (Exception e) {
            log.error("Error saving hike: {}", hike, e);
            throw e;
        }
    }

    public void updateHike(Hike hike) {
        try {
            hikeDAO.update(hike);
        } catch (Exception e) {
            log.error("Error updating hike: {}", hike, e);
            throw e;
        }
    }

    public void deleteHikeById(Integer id) {
        try {
            hikeDAO.deleteById(id);
        } catch (Exception e) {
            log.error("Error deleting hike by ID: {}", id, e);
            throw e;
        }
    }

    public List<Hike.HikeType> findAllHikeTypes() {
        try {
            return hikeDAO.findAllHikeTypes();
        } catch (Exception e) {
            log.error("Error fetching all hike types", e);
            throw e;
        }
    }
}
