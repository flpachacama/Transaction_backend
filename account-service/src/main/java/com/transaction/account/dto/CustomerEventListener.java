package com.transaction.account.listener;

import com.transaction.account.config.RabbitConfig;
import com.transaction.account.dto.CustomerCreatedEvent;
import com.transaction.account.entity.CustomerReference;
import com.transaction.account.repository.CustomerReferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerEventListener {

    private final CustomerReferenceRepository customerReferenceRepository;

    @RabbitListener(queues = RabbitConfig.CUSTOMER_CREATED_QUEUE)
    public void handleCustomerCreated(CustomerCreatedEvent event) {
        
        if (event == null) {
            log.warn("Received null customer event");
            return;
        }

        log.info("Received customer event: {}", event);

        if (event.getClientId() == null || event.getClientId().isBlank()) {
            log.warn("Customer event with invalid clientId: {}", event.getClientId());
            return;
        }

        if (event.getClientName() == null || event.getClientName().isBlank()) {
            log.warn("Customer event with invalid clientName: {}", event.getClientName());
            return;
        }

        try {
            log.debug("Processing customer created event for clientId: {}", event.getClientId());
            
            persistCustomerReference(event);
                    
        } catch (Exception e) {
            log.error("Error processing customer event: {}", event, e);
            throw new RuntimeException("Failed to process customer event", e);
        }
    }

    private void persistCustomerReference(CustomerCreatedEvent event) {
        var existingCustomer = customerReferenceRepository.findByClientId(event.getClientId());

        if (existingCustomer.isPresent()) {
            log.warn("Customer already exists with clientId: {}. Ignoring event.", event.getClientId());
            return;
        }

        CustomerReference newCustomer = new CustomerReference();
        newCustomer.setClientId(event.getClientId());
        newCustomer.setClientName(event.getClientName());
        newCustomer.setStatus(event.getStatus() != null ? event.getStatus() : true);

        customerReferenceRepository.save(newCustomer);
        log.info("Customer reference created successfully. ClientId: {}, ClientName: {}", 
                event.getClientId(), event.getClientName());
    }

}
