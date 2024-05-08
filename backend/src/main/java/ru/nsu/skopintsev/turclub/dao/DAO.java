package ru.nsu.skopintsev.turclub.dao;

import java.util.List;

public interface DAO<T, E> {
    List<T> findAll();
    T findById(E id);
    int save(T t);
    int update(T t);
    int deleteById(E id);
}
