package com.vuog.core.module.auth.infrastructure.persistence;

import com.vuog.core.module.auth.domain.model.Group;
import com.vuog.core.module.auth.domain.repository.GroupRepository;
import com.vuog.core.module.rest.domain.repository.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepositoryImpl extends GenericRepository<Group, Long>, GroupRepository {

    @Override
    default Class<Group> getEntityClass() {
        return Group.class;
    }
}
