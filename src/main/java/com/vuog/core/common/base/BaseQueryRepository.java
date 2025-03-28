package com.vuog.core.common.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;


public interface BaseQueryRepository<T> {

    T getById(Long id);

    Optional<T> findById(Long id);

    List<T> findAll(Specification<T> spec);

    Page<T> findAll(Specification<T> spec, Pageable pageable);

    long count(Specification<T> spec);
}
