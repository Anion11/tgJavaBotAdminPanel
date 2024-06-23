package com.example.adminpanel.DB.IDAO;

import java.util.Collection;

public interface IDAO <T> {
    Collection<T> getAll();
    T get(int id);
    void delete(int id);
    void create(T subscribe);
    void update(T subscribe);
}
