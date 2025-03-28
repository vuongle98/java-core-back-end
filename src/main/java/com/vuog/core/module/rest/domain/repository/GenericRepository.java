package com.vuog.core.module.rest.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository<T, Id> extends JpaRepository<T, Id>, JpaSpecificationExecutor<T> {
    Class<T> getEntityClass();
}
