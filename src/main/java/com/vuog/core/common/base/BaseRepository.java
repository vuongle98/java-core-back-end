package com.vuog.core.common.base;


public interface BaseRepository<T> {

    T save(T entity);

    <S extends T> Iterable<S> saveAll(Iterable<S> entities);

    void delete(T entity);

    void deleteById(Long id);

}
