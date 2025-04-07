package com.vuog.core.module.stream.infrastructure.messaging.producer;

import com.vuog.core.common.event.StreamMessage;
import com.vuog.core.module.stream.application.event.StreamPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class KafkaStreamPublisher implements StreamPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaStreamPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(String key, StreamMessage event) {
        if (Objects.nonNull(key)) {
            kafkaTemplate.send(event.topic(), key, event.toPayload());
        } else {
            kafkaTemplate.send(event.topic(), event.toPayload());
        }
    }
}
