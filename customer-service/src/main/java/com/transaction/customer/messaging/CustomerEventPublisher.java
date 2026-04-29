package com.transaction.customer.messaging;

import com.transaction.customer.entity.Customer;

public interface CustomerEventPublisher {

    void publishCustomerCreated(Customer customer);
}
