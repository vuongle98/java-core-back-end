package com.vuog.core.module.auth.domain.repository;

import com.vuog.core.common.base.BaseQueryRepository;
import com.vuog.core.common.base.BaseRepository;
import com.vuog.core.module.auth.domain.model.Group;

import java.util.Optional;

public interface GroupRepository extends
        BaseRepository<Group>,
        BaseQueryRepository<Group> {

    Optional<Group> findByName(String name);
    Optional<Group> findByCode(String code);

    Boolean existsByName(String name);

    Boolean existsByCode(String code);

}
