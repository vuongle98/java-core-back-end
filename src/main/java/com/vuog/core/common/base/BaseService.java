package com.vuog.core.common.base;


public interface BaseService<T, C> {

    T create(C command);

    T update(Long id, C command);

    void delete(Long id, boolean force);
}