package com.vuog.core.module.stream.infrastructure.messaging.producer;

import com.vuog.core.common.event.StreamMessage;
import com.vuog.core.module.stream.application.event.StreamPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaStreamPublisher implements StreamPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void publish(String key, StreamMessage event) {
        try {
            if (Objects.nonNull(key)) {
                kafkaTemplate.send(event.topic(), key, event.toPayload());
                log.debug("Published message to topic {} with key {}", event.topic(), key);
            } else {
                kafkaTemplate.send(event.topic(), event.toPayload());
                log.debug("Published message to topic {}", event.topic());
            }
        } catch (Exception e) {
            log.error("Failed to publish message to Kafka: {}", e.getMessage(), e);
        }
    }
}
