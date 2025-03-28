package com.vuog.core.common.base;


public interface BaseRepository<T> {

    T save(T entity);

    void delete(T entity);

    void deleteById(Long id);

}
