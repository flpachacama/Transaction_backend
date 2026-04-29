package com.transaction.account.repository;

import com.transaction.account.entity.Movement;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findByAccountId(Long accountId);
    List<Movement> findByMovementDateBetween(LocalDateTime start, LocalDateTime end);
}
