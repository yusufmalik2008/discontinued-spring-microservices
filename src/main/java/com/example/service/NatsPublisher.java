// src/main/java/com/example/service/NatsPublisher.java
package com.example.service;

import com.example.event.OrderCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import org.springframework.stereotype.Service;

@Service
public class NatsPublisher {

    private final Connection nats;
    private final ObjectMapper mapper = new ObjectMapper();

    public NatsPublisher(Connection nats) {
        this.nats = nats;
    }

    public void publishOrderCreated(OrderCreatedEvent event) throws JsonProcessingException {
        String json = mapper.writeValueAsString(event);
        nats.publish("orders.created", json.getBytes());
        System.out.println("Published orders.created → " + json);
    }
}
