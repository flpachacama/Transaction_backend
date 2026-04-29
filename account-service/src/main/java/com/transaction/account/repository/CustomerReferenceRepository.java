package com.transaction.account.repository;

import com.transaction.account.entity.CustomerReference;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerReferenceRepository extends JpaRepository<CustomerReference, Long> {
    Optional<CustomerReference> findByClientId(String clientId);
}
