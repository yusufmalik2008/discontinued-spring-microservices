package com.example.controller;

import com.example.event.OrderCreatedEvent;
import com.example.service.NatsPublisher;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final NatsPublisher publisher;

    public OrderController(NatsPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping
    public String createOrder(@RequestBody OrderRequest req) throws Exception {
        OrderCreatedEvent event = new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                req.customerName(),
                req.amount(),
                req.product()
        );

        publisher.publishOrderCreated(event);
        return "Order created! ID: " + event.getOrderId();
    }

    record OrderRequest(String customerName, Double amount, String product) {}
}
