package com.example.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderCreatedEvent {
    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("customerName")
    private String customerName;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("product")
    private String product;

    // === Required empty constructor for Jackson ===
    public OrderCreatedEvent() {}

    // === Constructor with all fields ===
    public OrderCreatedEvent(String orderId, String customerName, Double amount, String product) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.amount = amount;
        this.product = product;
    }

    // === Getters ===
    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public Double getAmount() { return amount; }
    public String getProduct() { return product; }

    // === Setters (needed for Jackson) ===
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setAmount(Double amount) { this.amount = amount; }
    public void setProduct(String product) { this.product = product; }

    @Override
    public String toString() {
        return "OrderCreatedEvent{" +
                "orderId='" + orderId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", amount=" + amount +
                ", product='" + product + '\'' +
                '}';
    }
}
