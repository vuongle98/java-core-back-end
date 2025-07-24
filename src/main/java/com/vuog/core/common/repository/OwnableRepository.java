package com.vuog.core.common.repository;

public interface OwnableRepository<T, ID> {
    boolean existsByIdAndCreatedBy(ID id, String createdBy);
}
