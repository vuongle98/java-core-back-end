package com.vuog.core.common.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BaseQueryService<T, Q> {

    Page<T> findAll(Q query, Pageable pageable);

    T getById(Long id);

    Optional<T> findById(Long id);

    List<T> getAll(Q query);
}
