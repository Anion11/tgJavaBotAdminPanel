package com.example.adminpanel.DB.IDAO;

import java.util.Collection;

public interface IDAO <T> {
    Collection<T> getAll();
    T get(Long id);
    void delete(Long id);
    void create(T subscribe);
    void update(T subscribe);
}
