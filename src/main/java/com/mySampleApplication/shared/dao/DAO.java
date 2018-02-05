package com.mySampleApplication.shared.dao;

import java.util.List;

public interface DAO<T> {
    long add(T obj);
    List<T> getList();
    T getById(long id);
    void deleteById(long id);
    void updateById(long id, T obj);
}
