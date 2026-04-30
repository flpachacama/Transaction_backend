package com.transaction.customer.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CustomerCreatedEvent(
        @JsonProperty("client_id")
        String clientId,
        @JsonProperty("client_name")
        String clientName,
        Boolean status) {
}
