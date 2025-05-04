package com.vuog.core.module.configuration.domain.service;

import com.vuog.core.module.configuration.domain.model.Configuration;

public interface ConfigurationDomainService {

    void checkExisted(Configuration configuration);
    void checkNotNull(Configuration configuration);
}
