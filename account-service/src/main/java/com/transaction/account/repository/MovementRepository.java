package com.transaction.account.repository;

import com.transaction.account.entity.Movement;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findByAccountIdAndDateBetweenOrderByDateAsc(Long accountId, LocalDateTime start, LocalDateTime end);
    List<Movement> findByAccountIdOrderByDateAsc(Long accountId);
}
