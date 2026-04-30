package com.transaction.customer.messaging;

import com.transaction.customer.entity.Customer;
import com.transaction.customer.util.CustomerConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitCustomerEventPublisher implements CustomerEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange customerExchange;

    @Override
    public void publishCustomerCreated(Customer customer) {
        CustomerCreatedEvent event = new CustomerCreatedEvent(customer.getClientId(), customer.getName(), customer.getStatus());
        rabbitTemplate.convertAndSend(customerExchange.getName(), CustomerConstants.CUSTOMER_CREATED_ROUTING_KEY, event);
        log.info("Customer event published: clientId={}, clientName={}", customer.getClientId(), customer.getName());
    }
}
