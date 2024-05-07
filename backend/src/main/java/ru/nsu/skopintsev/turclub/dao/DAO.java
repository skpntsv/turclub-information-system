package ru.nsu.skopintsev.turclub.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    void save(T t);
    void update(T t);
    void deleteById(int id);
}
