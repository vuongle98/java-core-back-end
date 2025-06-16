package com.vuog.core.module.stream.infrastructure.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for Kafka topics
 */
@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    /**
     * Topic for entity change events
     */
    @Bean
    public NewTopic entityChangesTopic() {
        return TopicBuilder.name("entity.changes")
                .partitions(3)
                .replicas(1)
                .build();
    }

    /**
     * Topic for log events
     */
    @Bean
    public NewTopic logTopic() {
        return TopicBuilder.name("log.general")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
