package com.vuog.core.common.base;

import java.time.Instant;

public interface BaseProjection {

    Instant getCreatedAt();
    Instant getUpdatedAt();
}
