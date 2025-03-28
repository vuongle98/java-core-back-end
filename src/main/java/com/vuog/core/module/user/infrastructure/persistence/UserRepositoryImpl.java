package com.vuog.core.module.user.infrastructure.persistence;

import com.vuog.core.module.rest.domain.repository.GenericRepository;
import com.vuog.core.module.user.domain.model.User;
import com.vuog.core.module.user.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryImpl extends GenericRepository<User, Long>, UserRepository {

    @Override
    default Class<User> getEntityClass() {
        return User.class;
    }
}
