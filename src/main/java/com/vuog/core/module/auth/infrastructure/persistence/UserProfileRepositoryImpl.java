package com.vuog.core.module.auth.infrastructure.persistence;

import com.vuog.core.module.rest.domain.repository.GenericRepository;
import com.vuog.core.module.auth.domain.model.UserProfile;
import com.vuog.core.module.auth.domain.repository.UserProfileRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepositoryImpl extends GenericRepository<UserProfile, Long>, UserProfileRepository {

    default Class<UserProfile> getEntityClass() {
        return UserProfile.class;
    }
}
