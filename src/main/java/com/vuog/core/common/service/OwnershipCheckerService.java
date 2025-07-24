package com.vuog.core.common.service;

import com.vuog.core.common.repository.OwnableRepository;
import com.vuog.core.common.util.Context;

public class OwnershipCheckerService {

    public <ID, T> boolean isOwner(OwnableRepository<T, ID> repo, ID id) {
        String userId = Context.getCurrentUsername();
        return repo.existsByIdAndCreatedBy(id, userId);
    }
}
