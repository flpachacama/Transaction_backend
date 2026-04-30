package com.transaction.customer.repository;

import java.util.Optional;
import com.transaction.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByClientId(String clientId);

    Optional<Customer> findByClientIdAndStatusTrue(String clientId);

    boolean existsByClientId(String clientId);
}
