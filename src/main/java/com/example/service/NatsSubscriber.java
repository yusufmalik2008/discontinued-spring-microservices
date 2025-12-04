// src/main/java/com/example/service/NatsSubscriber.java
package com.example.service;

import com.example.event.OrderCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class NatsSubscriber {

    private final Connection nats;
    private final ObjectMapper mapper = new ObjectMapper();

    public NatsSubscriber(Connection nats) {
        this.nats = nats;
    }

    @PostConstruct
    public void subscribe() throws Exception {
        JetStream js = nats.jetStream();

        PushSubscribeOptions opts = PushSubscribeOptions.builder()
                .durable("payment-service-durable")
                .build();

        JetStreamSubscription sub = js.subscribe("orders.created", opts);

        nats.flush(Duration.ofSeconds(5));
        System.out.println("JetStream durable consumer ready (persistence ON)");

        new Thread(() -> {
            while (true) {
                try {
                    Message msg = sub.nextMessage(Duration.ofSeconds(10));
                    if (msg == null) continue;

                    String payload = new String(msg.getData());
                    OrderCreatedEvent event = mapper.readValue(payload, OrderCreatedEvent.class);

                    System.out.println("Payment Service: Processing payment for order " + event.getOrderId());
                    System.out.println("Customer: " + event.getCustomerName() + ", Amount: $" + event.getAmount());
                    Thread.sleep(500); // now inside try-catch
                    System.out.println("Payment SUCCESSFUL for order " + event.getOrderId() + "\n");

                    msg.ack();
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt(); // restore interrupt status
                    break;
                } catch (Exception e) {
                    System.out.println("Failed to process message");
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
