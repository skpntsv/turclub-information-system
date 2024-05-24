package ru.nsu.skopintsev.turclub.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.skopintsev.turclub.dao.GroupsDAO;
import ru.nsu.skopintsev.turclub.models.Groups;

import java.util.List;

@Service
@Transactional
@Slf4j
public class GroupsService {

    private final GroupsDAO groupsDAO;

    @Autowired
    public GroupsService(GroupsDAO groupsDAO) {
        this.groupsDAO = groupsDAO;
    }

    public List<Groups> findAllGroups() {
        try {
            return groupsDAO.findAll();
        } catch (Exception e) {
            log.error("Error fetching all groups", e);
            throw e;
        }
    }

    public Groups findGroupById(Integer id) {
        try {
            return groupsDAO.findById(id);
        } catch (Exception e) {
            log.error("Error fetching group by ID: {}", id, e);
            throw e;
        }
    }
}
