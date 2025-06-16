# Stream Module

The Stream Module is a reusable component designed to handle publishing domain events and logs to Kafka following Domain-Driven Design (DDD) principles.

## Features

- Entity change tracking (create/update/delete)
- Structured logging to Kafka
- Reusable across different modules and services
- Compliant with DDD architecture

## Architecture

The module follows standard DDD layering:

### Domain Layer

- **Events**: Base event models (`BaseEvent`, `EntityChangedEvent`, `LogEvent`)
- **Services**: Domain service interfaces (`EventPublisher`, `EntityChangeTracker`, `LogPublisher`)

### Application Layer

- **Services**: Application services (`EntityChangeService`, `LoggingService`)
- **Events**: Event interfaces (`StreamPublisher`)

### Infrastructure Layer

- **Services**: Implementations of domain services (`KafkaEventPublisher`, `KafkaEntityChangeTracker`, `KafkaLogPublisher`)
- **Messaging**: Kafka-specific components (`KafkaStreamPublisher`, `KafkaStreamConsumer`)
- **Config**: Kafka configuration (`KafkaProducerConfig`, `KafkaTopicConfig`, `StreamConfig`)
- **Listeners**: JPA entity listeners (`JpaEntityChangeListener`)

## Usage

### Tracking Entity Changes

Entity changes are automatically tracked through JPA entity listeners. You can also manually track changes:

```java
@Service
public class MyService {
    private final EntityChangeService entityChangeService;

    public MyService(EntityChangeService entityChangeService) {
        this.entityChangeService = entityChangeService;
    }

    public Entity createEntity(Entity entity) {
        // Save entity
        entityChangeService.trackCreated(entity, "username");
        return entity;
    }
}
```

### Logging

```java
@Service
public class MyService {
    private final LoggingService loggingService;

    public MyService(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    public void doSomething() {
        loggingService.info("Operation completed", "operations");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("userId", 123);
        metadata.put("action", "login");
        loggingService.info("User logged in", "security", metadata);
    }
}
```

## Message Format

### Entity Change Event

```json
{
  "eventId": "uuid",
  "eventType": "EntityChangedEvent",
  "timestamp": "2023-06-15T10:15:30Z",
  "source": "core-service",
  "entityType": "User",
  "entityId": "123",
  "changeType": "CREATED",
  "entityData": "{...}",
  "actor": "admin"
}
```

### Log Event

```json
{
  "eventId": "uuid",
  "eventType": "LogEvent",
  "timestamp": "2023-06-15T10:15:30Z",
  "source": "core-service",
  "level": "INFO",
  "message": "User logged in",
  "category": "security",
  "metadata": {
    "userId": 123,
    "action": "login"
  }
}
```

## Extending

To extend the module for additional event types:

1. Create a new event class extending `BaseEvent`
2. Define the topic pattern in the `getTopic()` method
3. Add any additional fields needed for the event type

```java
public class MyCustomEvent extends BaseEvent {
    private String customField;

    @Override
    public String getTopic() {
        return "custom.events";
    }
}
```
