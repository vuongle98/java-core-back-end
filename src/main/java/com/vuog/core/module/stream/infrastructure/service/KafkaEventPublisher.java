package com.vuog.core.module.stream.infrastructure.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vuog.core.module.stream.domain.event.BaseEvent;
import com.vuog.core.module.stream.domain.service.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka implementation of EventPublisher
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(BaseEvent event) {
        event.init();
        publish(null, event);
    }

    @Override
    public void publish(String routingKey, BaseEvent event) {
        try {
            event.init();
            String topic = event.getTopic();
            String payload = objectMapper.writeValueAsString(event);

            if (routingKey != null && !routingKey.isEmpty()) {
                kafkaTemplate.send(topic, routingKey, payload);
                log.debug("Published event {} to topic {} with routing key {}", 
                    event.getEventType(), topic, routingKey);
            } else {
                kafkaTemplate.send(topic, payload);
                log.debug("Published event {} to topic {}", event.getEventType(), topic);
            }
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage(), e);
        }
    }
}
