package com.vuog.core.module.user.infrastructure.messaging;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public UserEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // public void publishUserCreatedEvent(User user) {
    //     applicationEventPublisher.publishEvent(new UserCreatedEvent(user));
    // }

    // public void publishUserUpdatedEvent(User user) {
    //     applicationEventPublisher.publishEvent(new UserUpdatedEvent(user));
    // }

}
