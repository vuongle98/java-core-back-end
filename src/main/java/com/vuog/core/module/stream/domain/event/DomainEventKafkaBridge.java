package com.vuog.core.module.stream.domain.event;

import com.vuog.core.common.event.DomainEvent;
import com.vuog.core.common.event.StreamMessage;
import com.vuog.core.module.stream.application.event.StreamPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class DomainEventKafkaBridge {

    private final StreamPublisher streamPublisher;

    @EventListener
    public void handle(DomainEvent event) {
        streamPublisher.publish(event.key(), new StreamMessage() {
            @Override
            public String topic() {
                return event.eventName();
            }

            @Override
            public String toPayload() {
                return event.toPayload();
            }
        });
    }
}
