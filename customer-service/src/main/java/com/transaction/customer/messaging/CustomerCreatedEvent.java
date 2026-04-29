package com.transaction.customer.messaging;

public record CustomerCreatedEvent(String clientId, String name, Boolean status) {
}
